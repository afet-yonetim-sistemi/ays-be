package org.ays.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class for enabling and setting up scheduling in the AYS application.
 * <p>
 * This class configures the scheduling infrastructure for the application and loads
 * scheduling-related properties from a specified configuration file.
 * </p>
 */
@Configuration
@EnableScheduling
@PropertySource("classpath:application.yml")
class AysSpringSchedulerConfiguration {
}
