package org.ays.common.client;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public interface AysCacheClient {

    Optional<String> find(String prefix, String key);

    void put(String prefix, String key, String value, Duration timeToLive);

    void putAll(String prefix, Map<String, String> data, Duration timeToLive);

    void remove(String prefix, String key);

}
