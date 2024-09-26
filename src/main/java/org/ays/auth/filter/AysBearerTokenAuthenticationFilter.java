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
import org.ays.auth.service.AysInvalidTokenService;
import org.ays.auth.service.AysTokenService;
import org.ays.common.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class AysBearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AysTokenService tokenService;
    private final AysInvalidTokenService invalidTokenService;


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

        final String ipAddress = HttpServletRequestUtil.getClientIpAddress(httpServletRequest);

        final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (AysToken.isBearerToken(authorizationHeader)) {
            final String jwt = AysToken.getJwt(authorizationHeader);

            tokenService.verifyAndValidate(jwt);

            final String tokenId = tokenService.getPayload(jwt).getId();
            invalidTokenService.checkForInvalidityOfToken(tokenId);

            if (this.isNotAllowedPath(httpServletRequest) || isAuthorizedRateLimitEnabled) {
                boolean isRateLimitExceeded = this.isRateLimitExceeded(ipAddress, authorizedBuckets, httpServletResponse);
                if (isRateLimitExceeded) {
                    return;
                }
            }

            final var authentication = tokenService.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


        if (this.isNotAllowedPath(httpServletRequest) && isUnauthorizedRateLimitEnabled) {
            boolean isRateLimitExceeded = this.isRateLimitExceeded(ipAddress, unauthorizedBuckets, httpServletResponse);
            if (isRateLimitExceeded) {
                return;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    private boolean isNotAllowedPath(final HttpServletRequest httpServletRequest) {
        final String requestURI = httpServletRequest.getRequestURI();
        return ALLOWED_PATHS.stream()
                .noneMatch(requestURI::startsWith);
    }

    private boolean isRateLimitExceeded(final String ipAddress,
                                        final LoadingCache<String, Bucket> buckets,
                                        final HttpServletResponse httpServletResponse) {

        try {

            final Bucket bucket = buckets.get(ipAddress);
            if (bucket.tryConsume(1)) {
                return false;
            }

        } catch (ExecutionException exception) {
            log.error("Error while checking rate limit for IP: {}", ipAddress, exception);
            httpServletResponse.setStatus(429);
            return true;
        }

        log.warn("Rate limit exceeded for IP: {}", ipAddress);
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
