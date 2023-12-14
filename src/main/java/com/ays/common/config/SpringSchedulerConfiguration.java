package com.ays.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class for Spring Scheduler.
 * This class provides a custom Spring Scheduler configurations for the AYS software solutions.
 */
@Configuration
@EnableScheduling
@PropertySource("classpath:application.yml")
class SpringSchedulerConfiguration {
}
