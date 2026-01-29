package org.ays.common.client.impl;

import lombok.RequiredArgsConstructor;
import org.ays.common.client.AysCacheClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class AysCacheClientRedisImpl implements AysCacheClient {

    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public Optional<String> find(final String key) {
        return Optional.ofNullable(
                stringRedisTemplate.opsForValue().get(key)
        );
    }


    @Override
    public void put(final String key,
                    final String value,
                    final Duration timeToLive) {

        stringRedisTemplate.opsForValue().set(key, value, timeToLive);
    }


    @Override
    public void putAll(final Map<String, String> data,
                       final Duration timeToLive) {

        data.forEach((key, value) -> this.put(key, value, timeToLive));
    }


    @Override
    public void remove(final String key) {
        stringRedisTemplate.delete(key);
    }

}
