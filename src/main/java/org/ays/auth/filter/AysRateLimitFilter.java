package org.ays.auth.filter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ays.auth.model.AysToken;
import org.ays.common.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * AysBearerTokenAuthenticationFilter is a filter that intercepts HTTP requests and processes the Bearer tokens included in the Authorization headers.
 * If the token is valid, the user is authenticated and added to the SecurityContext for the duration of the request.
 * If the token is invalid, a 401 Unauthorized response is returned.
 * <p>The filter uses an instance of AysTokenService to verify and validate the token and retrieve the user authentication.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AysRateLimitFilter extends OncePerRequestFilter {

    @Value("${ays.rate-limit.authorized.enabled}")
    private boolean isAuthorizedRateLimitEnabled;

    private static final int MAXIMUM_AUTHORIZED_REQUESTS_COUNTS = 20;
    private static final int MAXIMUM_AUTHORIZED_REQUESTS_DURATION_MINUTES = 1;
    private static final Duration MAXIMUM_AUTHORIZED_REQUESTS_DURATION = Duration.ofMinutes(MAXIMUM_AUTHORIZED_REQUESTS_DURATION_MINUTES);
    private final LoadingCache<String, Bucket> authorizedBuckets = CacheBuilder.newBuilder()
            .expireAfterWrite(MAXIMUM_AUTHORIZED_REQUESTS_DURATION_MINUTES, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull Bucket load(@NotNull String key) {
                    return newBucket(
                            MAXIMUM_AUTHORIZED_REQUESTS_COUNTS,
                            MAXIMUM_AUTHORIZED_REQUESTS_DURATION
                    );
                }
            });


    @Value("${ays.rate-limit.unauthorized.enabled}")
    private boolean isUnauthorizedRateLimitEnabled;

    private static final int MAXIMUM_UNAUTHORIZED_REQUESTS_COUNTS = 5;
    private static final int MAXIMUM_UNAUTHORIZED_REQUESTS_DURATION_MINUTES = 10;
    private static final Duration MAXIMUM_UNAUTHORIZED_REQUESTS_DURATION = Duration.ofMinutes(MAXIMUM_UNAUTHORIZED_REQUESTS_DURATION_MINUTES);
    private final LoadingCache<String, Bucket> unauthorizedBuckets = CacheBuilder.newBuilder()
            .expireAfterWrite(MAXIMUM_UNAUTHORIZED_REQUESTS_DURATION_MINUTES, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull Bucket load(@NotNull String key) {
                    return newBucket(
                            MAXIMUM_UNAUTHORIZED_REQUESTS_COUNTS,
                            MAXIMUM_UNAUTHORIZED_REQUESTS_DURATION
                    );
                }
            });


    private static final List<String> ALLOWED_PATHS = List
            .of("/public/actuator");


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        boolean rateLimitExceeded = this.isRateLimitExceeded(
                authorizationHeader,
                httpServletRequest,
                httpServletResponse
        );

        if (rateLimitExceeded) {
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean isRateLimitExceeded(final String authorizationHeader,
                                        final HttpServletRequest httpServletRequest,
                                        final HttpServletResponse httpServletResponse) {

        final String endpoint = httpServletRequest.getRequestURI();
        boolean isAllowedPath = ALLOWED_PATHS.stream()
                .anyMatch(endpoint::startsWith);

        boolean isRateLimitEnabled = isAuthorizedRateLimitEnabled || isUnauthorizedRateLimitEnabled;

        if (isAllowedPath || !isRateLimitEnabled) {
            return false;
        }

        if (AysToken.isBearerToken(authorizationHeader)) {
            return this.isRateLimitExceededOnBuckets(authorizedBuckets, httpServletRequest, httpServletResponse);
        }

        return this.isRateLimitExceededOnBuckets(unauthorizedBuckets, httpServletRequest, httpServletResponse);
    }

    private boolean isRateLimitExceededOnBuckets(final LoadingCache<String, Bucket> buckets,
                                                 final HttpServletRequest httpServletRequest,
                                                 final HttpServletResponse httpServletResponse) {

        final String ipAddress = HttpServletRequestUtil.getClientIpAddress(httpServletRequest);

        try {

            final Bucket bucket = buckets.get(ipAddress);
            if (bucket.tryConsume(1)) {
                return false;
            }

        } catch (ExecutionException exception) {
            final String method = httpServletRequest.getMethod();
            final String endpoint = httpServletRequest.getRequestURI();
            log.error("Error while checking rate limit by {} to {} - {}", ipAddress, method, endpoint);
            httpServletResponse.setStatus(429);
            return true;
        }

        final String method = httpServletRequest.getMethod();
        final String endpoint = httpServletRequest.getRequestURI();
        log.warn("Rate limit exceeded by {} to {} - {}", ipAddress, method, endpoint);
        httpServletResponse.setStatus(429);
        return true;
    }

    private static Bucket newBucket(int maximumRequestsCounts, Duration maximumDuration) {
        final Bandwidth bandwidth = Bandwidth
                .builder()
                .capacity(maximumRequestsCounts)
                .refillIntervally(maximumRequestsCounts, maximumDuration)
                .build();
        return Bucket.builder()
                .addLimit(bandwidth)
                .build();
    }

}
