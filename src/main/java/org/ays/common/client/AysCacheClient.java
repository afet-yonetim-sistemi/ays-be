package org.ays.common.client;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Contract for a cache client that stores and retrieves String values.
 * <p>
 * This interface defines the operations required to read, write, and remove cached entries.
 * How keys are composed, where data is stored, and how expiration is enforced are implementation details.
 * </p>
 */
public interface AysCacheClient {

    /**
     * Retrieves a cached value using the given identifiers.
     *
     * @param prefix a logical grouping identifier for the entry
     * @param key    the entry identifier within the given prefix
     * @return an {@link Optional} containing the cached value if present; otherwise {@link Optional#empty()}
     */
    Optional<String> find(String prefix, String key);

    /**
     * Stores a value in the cache with an expiration duration.
     *
     * @param prefix     a logical grouping identifier for the entry
     * @param key        the entry identifier within the given prefix
     * @param value      the value to cache
     * @param timeToLive the duration after which the entry may expire
     */
    void put(String prefix, String key, String value, Duration timeToLive);

    /**
     * Stores multiple values in the cache under the same logical grouping with the same expiration duration.
     *
     * @param prefix     a logical grouping identifier for the entries
     * @param data       the values to cache, mapped by key
     * @param timeToLive the duration after which the entries may expire
     */
    void putAll(String prefix, Map<String, String> data, Duration timeToLive);

    /**
     * Removes a cached value using the given identifiers.
     *
     * @param prefix a logical grouping identifier for the entry
     * @param key    the entry identifier within the given prefix
     */
    void remove(String prefix, String key);

}
