package org.ays.common.client.impl;

import lombok.RequiredArgsConstructor;
import org.ays.common.client.AysCacheClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Redis-backed implementation of {@link AysCacheClient} that stores and retrieves String values
 * using Spring’s {@link org.springframework.data.redis.core.StringRedisTemplate}.
 * <p>
 * Keys are composed as {@code <prefix>:<key>} to provide simple namespacing. Writes via
 * {@link #put(String, String, String, java.time.Duration)} and {@link #putAll(String, java.util.Map, java.time.Duration)}
 * apply a per-entry TTL. Reads return {@link java.util.Optional#empty()} when the entry is absent or expired.
 * <br>
 * Operations are non-transactional and executed individually; Redis driver/runtime exceptions
 * (e.g., {@link org.springframework.dao.DataAccessException}) may propagate to callers.
 * </p>
 *
 * @implNote This implementation is optimized for straightforward cache usage (idempotent reads/writes)
 * and does not perform batching or pipelining. The {@code prefix} is not validated—ensure callers
 * provide a sane namespace (no trailing colons).
 * @see AysCacheClient
 * @see org.springframework.data.redis.core.StringRedisTemplate
 */
@Component
@RequiredArgsConstructor
class AysCacheClientRedisImpl implements AysCacheClient {

    private final StringRedisTemplate stringRedisTemplate;


    /**
     * Retrieves a cached value from Redis by composing the key as {@code <prefix>:<key>}.
     *
     * @param prefix a logical namespace used to scope keys (e.g., feature or domain name)
     * @param key    the raw key within the given prefix
     * @return an {@link Optional} containing the cached value if present; otherwise {@link Optional#empty()}
     */
    @Override
    public Optional<String> find(final String prefix,
                                 final String key) {

        final String keyWithPrefix = mergePrefixAndKey(prefix, key);
        return Optional.ofNullable(
                stringRedisTemplate.opsForValue().get(keyWithPrefix)
        );
    }


    /**
     * Stores a value in Redis with a time-to-live (TTL), using the composed key {@code <prefix>:<key>}.
     *
     * @param prefix     a logical namespace used to scope keys (e.g., feature or domain name)
     * @param key        the raw key within the given prefix
     * @param value      the value to cache
     * @param timeToLive the expiration duration after which the entry is evicted
     */
    @Override
    public void put(final String prefix,
                    final String key,
                    final String value,
                    final Duration timeToLive) {

        final String keyWithPrefix = mergePrefixAndKey(prefix, key);
        stringRedisTemplate.opsForValue().set(keyWithPrefix, value, timeToLive);
    }


    /**
     * Stores multiple key–value pairs in Redis under the same prefix, applying the same TTL to each entry.
     * <p>
     * Each entry is written individually using {@link #put(String, String, String, Duration)} with the key
     * composed as {@code <prefix>:<key>}.
     * </p>
     *
     * @param prefix     a logical namespace used to scope keys for all provided entries
     * @param data       a map of raw keys to values to be cached
     * @param timeToLive the expiration duration applied to every cached entry
     */
    @Override
    public void putAll(final String prefix,
                       final Map<String, String> data,
                       final Duration timeToLive) {

        data.forEach((key, value) -> {
            this.put(prefix, key, value, timeToLive);
        });
    }


    /**
     * Removes a cached entry from Redis by composing the key as {@code <prefix>:<key>}.
     *
     * @param prefix a logical namespace used to scope keys
     * @param key    the raw key within the given prefix to delete
     */
    @Override
    public void remove(final String prefix,
                       final String key) {

        final String keyWithPrefix = mergePrefixAndKey(prefix, key);
        stringRedisTemplate.delete(keyWithPrefix);
    }

    /**
     * Concatenates the prefix and key into a single Redis key in the form {@code <prefix>:<key>}.
     *
     * @param prefix the logical namespace
     * @param key    the raw key
     * @return the composed Redis key
     */
    private static String mergePrefixAndKey(final String prefix,
                                            final String key) {
        return prefix + ":" + key;
    }

}
