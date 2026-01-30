package org.ays.common.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Enables caching support for the application.
 * <p>
 * Declares the caching infrastructure by activating Spring's cache abstraction.
 * Cache providers, cache managers, and cache usage are configured elsewhere.
 * </p>
 */
@Configuration
@EnableCaching
class AysCacheConfiguration {
}
