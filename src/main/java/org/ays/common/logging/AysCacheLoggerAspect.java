package org.ays.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


/**
 * AysCacheLoggerAspect provides logging functionality for cache-related operations within
 * methods of adapter classes. This aspect ensures that key events such as cache misses and
 * cache evictions are logged in a consistent and structured manner.
 * <p>
 * It leverages annotations such as {@link Cacheable} and
 * {@link CacheEvict} to identify and handle relevant cache interactions.
 */
@Slf4j
@Aspect
@Component
class AysCacheLoggerAspect {

    /**
     * Intercepts the specific cache miss methods in adapters to ensure a standard logging format.
     */
    @Before("execution(* org.ays..*Adapter.*(..)) && @annotation(org.springframework.cache.annotation.Cacheable)")
    public void logCacheMiss(JoinPoint joinPoint) {
        if (log.isWarnEnabled()) {
            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            log.warn(
                    "Cache miss detected for [{}::{}]. Value not found in cache, invoking method and loading data from persistence layer.",
                    className,
                    methodName
            );
        }
    }

    /**
     * Logs cache eviction events after successful execution of methods annotated with {@link CacheEvict}.
     * This ensures that any modification that leads to cache clearing is logged.
     *
     * @param joinPoint  the join point representing the intercepted method
     */
    @AfterReturning("execution(* org.ays..*Adapter.*(..)) && @annotation(org.springframework.cache.annotation.CacheEvict)")
    public void logCacheEvict(JoinPoint joinPoint) {
        if (log.isWarnEnabled()) {
            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            log.warn(
                    "Cache invalidated for [{}::{}]. Related cache entries cleared after data modification.",
                    className,
                    methodName
            );
        }
    }
}