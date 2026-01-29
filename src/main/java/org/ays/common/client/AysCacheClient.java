package org.ays.common.client;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public interface AysCacheClient {

    Optional<String> find(String key);

    void put(String key, String value, Duration timeToLive);

    void putAll(Map<String, String> data, Duration timeToLive);

    void remove(String key);

}
