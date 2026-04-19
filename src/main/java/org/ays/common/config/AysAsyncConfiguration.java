package org.ays.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

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
public class AysAsyncConfiguration {
    @Bean(name = "mailTaskExecutor")
    public Executor mailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setQueueCapacity(100);
        executor.setMaxPoolSize(8);
        executor.setThreadNamePrefix("AysAsync-");
        executor.initialize();
        return executor;
    }
}
