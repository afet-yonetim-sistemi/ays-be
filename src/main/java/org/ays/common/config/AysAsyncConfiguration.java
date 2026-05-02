package org.ays.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <p>
 * Enables asynchronous method execution in the application.
 * </p>
 * <p>
 * Allows methods annotated with {@code @Async} to run in separate threads.
 * </p>
 */
@Configuration
@EnableAsync
class AysAsyncConfiguration {
}
