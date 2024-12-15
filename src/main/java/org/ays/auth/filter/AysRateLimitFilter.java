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
import org.ays.common.model.request.AysHttpHeader;
import org.ays.common.model.request.AysHttpServletRequest;
import org.ays.common.model.response.AysHttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * AysRateLimitFilter is a filter that enforces rate-limiting policies for both
 * authorized and unauthorized users based on predefined configurations.
 * <p>
 * This filter limits the number of requests that can be made by a client
 * within a specific time window. It uses IP-based identification for unauthorized
 * users and token-based identification for authorized users. Requests exceeding
 * the rate limit receive a 429 (Too Many Requests) response.
 * <p>
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Separate rate limits for authorized and unauthorized users.</li>
 *     <li>Customizable rate limit counts and durations via configuration.</li>
 *     <li>Exemptions for specific paths (e.g., actuator endpoints).</li>
 *     <li>Uses {@link Bucket} for efficient rate-limiting mechanisms.</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * This filter is automatically applied as part of Spring's filter chain
 * with an order of {@code 2}, ensuring it is executed after audit logs
 * but before authentication or other security checks.
 */
@Slf4j
@Order(2)
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


    /**
     * Processes incoming HTTP requests to enforce rate-limiting policies.
     * <p>
     * Determines if the request exceeds the configured rate limits for the client
     * (authorized or unauthorized) and either allows the request to proceed or
     * blocks it with a 429 status code.
     *
     * @param httpServletRequest  the incoming HTTP request
     * @param httpServletResponse the HTTP response
     * @param filterChain         the filter chain to pass the request along if allowed
     * @throws ServletException if an error occurs during request processing
     * @throws IOException      if an I/O error occurs during request handling
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final AysHttpServletRequest aysHttpServletRequest = (AysHttpServletRequest) httpServletRequest;
        final AysHttpHeader aysHttpHeader = aysHttpServletRequest.getHeader();
        final AysHttpServletResponse aysHttpServletResponse = (AysHttpServletResponse) httpServletResponse;

        boolean rateLimitExceeded = this.isRateLimitExceeded(
                aysHttpServletRequest,
                aysHttpHeader,
                aysHttpServletResponse
        );

        if (rateLimitExceeded) {
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * Checks if the rate limit has been exceeded for the current request.
     * <p>
     * Determines whether the request falls under the authorized or unauthorized
     * rate-limiting rules and validates against the appropriate bucket.
     *
     * @param aysHttpServletRequest  the wrapped HTTP request
     * @param aysHttpHeader          the request headers
     * @param aysHttpServletResponse the wrapped HTTP response
     * @return {@code true} if the rate limit is exceeded; {@code false} otherwise
     */
    private boolean isRateLimitExceeded(final AysHttpServletRequest aysHttpServletRequest,
                                        final AysHttpHeader aysHttpHeader,
                                        final AysHttpServletResponse aysHttpServletResponse) {

        final String endpoint = aysHttpServletRequest.getPath();
        boolean isAllowedPath = ALLOWED_PATHS.stream()
                .anyMatch(endpoint::startsWith);

        boolean isRateLimitEnabled = isAuthorizedRateLimitEnabled || isUnauthorizedRateLimitEnabled;

        if (isAllowedPath || !isRateLimitEnabled) {
            return false;
        }

        if (aysHttpHeader.hasBearerToken()) {
            return this.isRateLimitExceededOnBuckets(authorizedBuckets, aysHttpServletRequest, aysHttpServletResponse);
        }

        return this.isRateLimitExceededOnBuckets(unauthorizedBuckets, aysHttpServletRequest, aysHttpServletResponse);
    }

    /**
     * Checks if the rate limit has been exceeded for the given buckets.
     * <p>
     * Attempts to consume a token from the bucket associated with the client's IP
     * address. If tokens are unavailable, the request is blocked.
     *
     * @param buckets                the rate-limiting buckets
     * @param aysHttpServletRequest  the wrapped HTTP request
     * @param aysHttpServletResponse the wrapped HTTP response
     * @return {@code true} if the rate limit is exceeded; {@code false} otherwise
     */
    private boolean isRateLimitExceededOnBuckets(final LoadingCache<String, Bucket> buckets,
                                                 final AysHttpServletRequest aysHttpServletRequest,
                                                 final AysHttpServletResponse aysHttpServletResponse) {

        final String ipAddress = aysHttpServletRequest.getHeader().getIpAddress();

        try {

            final Bucket bucket = buckets.get(ipAddress);
            if (bucket.tryConsume(1)) {
                return false;
            }

        } catch (ExecutionException exception) {
            final String method = aysHttpServletRequest.getMethod();
            final String endpoint = aysHttpServletRequest.getPath();
            log.error("Error while checking rate limit by {} to {} - {}", ipAddress, method, endpoint);
            aysHttpServletResponse.setStatus(429);
            return true;
        }

        final String method = aysHttpServletRequest.getMethod();
        final String endpoint = aysHttpServletRequest.getPath();
        log.warn("Rate limit exceeded by {} to {} - {}", ipAddress, method, endpoint);
        aysHttpServletResponse.setStatus(429);
        return true;
    }

    /**
     * Creates a new rate-limiting bucket with the specified maximum request counts
     * and duration.
     *
     * @param maximumRequestsCounts the maximum number of requests allowed within the duration
     * @param maximumDuration       the duration of the rate-limiting window
     * @return a new {@link Bucket} configured with the specified parameters
     */
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
