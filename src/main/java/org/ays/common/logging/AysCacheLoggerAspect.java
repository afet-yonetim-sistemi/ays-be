package org.ays.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


/**
 * Aspect for logging cache miss events in a standardized format.
 * This aspect intercepts methods annotated with {@code @Cacheable} within classes
 * that match the specified pointcut expression, ensuring cache-related operations
 * are logged for debugging purposes.
 * <p>
 * This can help trace and debug scenarios where cache misses occur, providing insights
 * into method invocations that lead to database access due to absence of cached data.
 * <p>
 * An example of a pointcut expression used:
 * - It matches any method execution within classes ending in "Adapter" under the
 *   {@code org.ays..} package and sub-packages, annotated with {@code @Cacheable}.
 * <p>
 * Dependencies:
 * 1. Lombok's {@code @Slf4j} for logging support.
 * 2. Spring AOP's {@code @Aspect} for defining aspect-oriented behavior.
 * 3. Spring Framework's {@code @Component} to mark the aspect as a Spring-managed bean.
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
        if (log.isDebugEnabled()) {
            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            log.debug("Cache miss for [{}::{}] - Method invoked. Loading from DB.", className, methodName);
        }
    }
}