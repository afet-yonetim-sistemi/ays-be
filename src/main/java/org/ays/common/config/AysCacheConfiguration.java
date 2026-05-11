package org.ays.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Configuration class for setting up caching using Redis in the AYS application.
 * <p>
 * This class enables caching support and configures a Redis-based cache manager
 * leveraging the Lettuce connection factory. It specifies connection details and
 * customizes serialization for cached data to ensure proper handling of complex
 * object structures.
 */
@Configuration
@EnableCaching
class AysCacheConfiguration {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    /**
     * Configures and provides a {@link RedisCacheManager} bean for managing Redis caches.
     * The cache manager uses a custom configuration to create Redis caches with different
     * time-to-live (TTL) durations, based on the cache name.
     *
     * @param connectionFactory the {@link RedisConnectionFactory} used to establish connections
     *                          with the Redis server
     * @return a {@link RedisCacheManager} instance configured with a default cache configuration
     *         and custom logic for determining TTL based on cache name patterns
     */
    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        GenericJackson2JsonRedisSerializer serializer = createSerializer();

        RedisCacheConfiguration defaultCacheConfig = createCacheConfig(serializer);

        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                defaultCacheConfig
        ) {
            @NonNull
            @Override
            protected RedisCache createRedisCache(@NonNull String name, RedisCacheConfiguration cacheConfig) {
                if (name.contains("#")) {
                    String[] parts = name.split("#");
                    String cacheName = parts[0];
                    String ttlString = parts[1].toLowerCase();
                    Duration duration = parseDuration(ttlString);

                    return super.createRedisCache(cacheName, cacheConfig.entryTtl(duration));
                }

                return super.createRedisCache(name, cacheConfig);
            }
        };
    }

    /**
     * Parses a duration string and converts it into a {@link Duration} object.
     * The input string is expected to end with a time unit character ('s' for seconds,
     * 'm' for minutes, 'h' for hours, 'd' for days) and may contain numeric values
     * representing the duration.
     * If the input string is invalid or contains an unrecognized format, a default
     * duration of one hour is returned. If no valid duration can be determined, {@link Duration#ZERO} is returned.
     *
     * @param ttlString the string representing the duration, which may include a numeric value
     *                  followed by a time unit ('s', 'm', 'h', or 'd')
     * @return a {@link Duration} object corresponding to the input string, or a default value
     *         if the input is invalid
     */
    private Duration parseDuration(String ttlString) {
        try {
            String value = ttlString.replaceAll("[^0-9]", "");
            long amount = Long.parseLong(value);

            if (ttlString.endsWith("s")) return Duration.ofSeconds(amount);
            if (ttlString.endsWith("m")) return Duration.ofMinutes(amount);
            if (ttlString.endsWith("h")) return Duration.ofHours(amount);
            if (ttlString.endsWith("d")) return Duration.ofDays(amount);

        } catch (Exception e) {
            return Duration.ofHours(1);
        }
        return Duration.ZERO;
    }

    /**
     * Creates a {@link RedisCacheConfiguration} instance with custom settings.
     * The configuration includes:
     * - Setting a default time-to-live (TTL) of {@link Duration#ZERO}, which indicates no expiration.
     * - Disabling caching of null values.
     * - Using a custom serializer for value serialization.
     *
     * @param serializer the {@link GenericJackson2JsonRedisSerializer} used to serialize cache values
     * @return a customized {@link RedisCacheConfiguration} instance
     */
    private RedisCacheConfiguration createCacheConfig(GenericJackson2JsonRedisSerializer serializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ZERO)
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                );
    }

    /**
     * Creates and configures an instance of {@link GenericJackson2JsonRedisSerializer}.
     * The serializer is initialized with a customized {@link ObjectMapper} that:
     * - Registers the {@link JavaTimeModule} to handle Java 8 date and time types.
     * - Disables writing dates as timestamps.
     * - Activates default typing to support polymorphic type handling for non-final types.
     *
     * @return a configured {@link GenericJackson2JsonRedisSerializer} instance
     */
    private GenericJackson2JsonRedisSerializer createSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}
