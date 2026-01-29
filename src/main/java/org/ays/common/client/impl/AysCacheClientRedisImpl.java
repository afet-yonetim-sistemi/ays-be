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
    public Optional<String> find(final String prefix,
                                 final String key) {

        final String keyWithPrefix = mergePrefixAndKey(prefix, key);
        return Optional.ofNullable(
                stringRedisTemplate.opsForValue().get(keyWithPrefix)
        );
    }


    @Override
    public void put(final String prefix,
                    final String key,
                    final String value,
                    final Duration timeToLive) {

        final String keyWithPrefix = mergePrefixAndKey(prefix, key);
        stringRedisTemplate.opsForValue().set(keyWithPrefix, value, timeToLive);
    }


    @Override
    public void putAll(final String prefix,
                       final Map<String, String> data,
                       final Duration timeToLive) {

        data.forEach((key, value) -> {
            this.put(prefix, key, value, timeToLive);
        });
    }


    @Override
    public void remove(final String prefix,
                       final String key) {

        final String keyWithPrefix = mergePrefixAndKey(prefix, key);
        stringRedisTemplate.delete(keyWithPrefix);
    }

    private static String mergePrefixAndKey(String prefix, String key) {
        return prefix + ":" + key;
    }

}
