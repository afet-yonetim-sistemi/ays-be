package org.ays.common.client.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.ays.AysUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

class AysCacheClientRedisImplTest extends AysUnitTest {

    @InjectMocks
    AysCacheClientRedisImpl cacheClientRedis;

    @Mock
    StringRedisTemplate stringRedisTemplate;

    @Mock
    ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp() {
        Mockito.when(stringRedisTemplate.opsForValue())
                .thenReturn(this.valueOperations);
    }


    /**
     * {@link AysCacheClientRedisImpl#find(String, String)}
     */
    @Test
    void givenPrefixAndKey_whenValueExists_thenReturnValueAsOptional() {

        // Given
        String mockPrefix = "prefix";
        String mockKey = "key";

        // When
        String mockValue = "value";
        String keyWithPrefix = mergePrefixAndKey(mockPrefix, mockKey);
        Mockito.when(valueOperations.get(keyWithPrefix))
                .thenReturn(mockValue);

        // Then
        Optional<String> value = cacheClientRedis.find(mockPrefix, mockKey);

        Assertions.assertTrue(value.isPresent());
        Assertions.assertEquals(mockValue, value.get());

        // Verify
        Mockito.verify(stringRedisTemplate, Mockito.times(1))
                .opsForValue();

        Mockito.verify(valueOperations, Mockito.times(1))
                .get(keyWithPrefix);
    }

    @Test
    void givenPrefixAndKey_whenValueDoesNotExist_thenReturnOptionalEmpty() {

        // Given
        String mockPrefix = "prefix";
        String mockKey = "key";

        // When
        String keyWithPrefix = mergePrefixAndKey(mockPrefix, mockKey);
        Mockito.when(valueOperations.get(keyWithPrefix))
                .thenReturn(null);

        // Then
        Optional<String> value = cacheClientRedis.find(mockPrefix, mockKey);

        Assertions.assertTrue(value.isEmpty());

        // Verify
        Mockito.verify(stringRedisTemplate, Mockito.times(1))
                .opsForValue();

        Mockito.verify(valueOperations, Mockito.times(1))
                .get(keyWithPrefix);
    }


    /**
     * {@link AysCacheClientRedisImpl#put(String, String, String, Duration)}
     */
    @Test
    void givenPrefixAndKeyAndValueAndTimeToLive_whenPut_thenReturnOptionalEmpty() {

        // Given
        String mockPrefix = "prefix";
        String mockKey = "key";
        String mockValue = "value";
        Duration mockTimeToLive = Duration.ofSeconds(10);

        // When
        String keyWithPrefix = mergePrefixAndKey(mockPrefix, mockKey);
        Mockito.doNothing()
                .when(valueOperations)
                .set(keyWithPrefix, mockValue, mockTimeToLive);

        // Then
        cacheClientRedis.put(mockPrefix, mockKey, mockValue, mockTimeToLive);

        // Verify
        Mockito.verify(stringRedisTemplate, Mockito.times(1))
                .opsForValue();

        Mockito.verify(valueOperations, Mockito.times(1))
                .set(keyWithPrefix, mockValue, mockTimeToLive);
    }


    /**
     * {@link AysCacheClientRedisImpl#putAll(String, Map, Duration)}
     */
    @Test
    void givenPrefixAndKeyAndValueAndTimeToLive_whenPutAll_thenDoNothing() {

        // Given
        String mockPrefix = "prefix";
        Map<String, String> mockData = Map.of(
                RandomStringUtils.secure().nextAlphabetic(8), RandomStringUtils.secure().nextAlphabetic(8),
                RandomStringUtils.secure().nextAlphabetic(8), RandomStringUtils.secure().nextAlphabetic(8),
                RandomStringUtils.secure().nextAlphabetic(8), RandomStringUtils.secure().nextAlphabetic(8),
                RandomStringUtils.secure().nextAlphabetic(8), RandomStringUtils.secure().nextAlphabetic(8)
        );
        Duration mockTimeToLive = Duration.ofSeconds(10);

        // When
        mockData.forEach((mockKey, mockValue) -> {
            String keyWithPrefix = mergePrefixAndKey(mockPrefix, mockKey);
            Mockito.doNothing()
                    .when(valueOperations)
                    .set(keyWithPrefix, mockValue, mockTimeToLive);
        });

        // Then
        cacheClientRedis.putAll(mockPrefix, mockData, mockTimeToLive);

        // Verify
        Mockito.verify(stringRedisTemplate, Mockito.times(mockData.size()))
                .opsForValue();

        mockData.forEach((mockKey, mockValue) -> {
            String keyWithPrefix = mergePrefixAndKey(mockPrefix, mockKey);
            Mockito.verify(valueOperations, Mockito.times(1))
                    .set(keyWithPrefix, mockValue, mockTimeToLive);
        });
    }


    /**
     * {@link AysCacheClientRedisImpl#remove(String, String)}
     */
    @Test
    void givenPrefixAndKeyAndValueAndTimeToLive_whenRemoved_thenDoNothing() {

        // Given
        String mockPrefix = "prefix";
        String mockKey = "key";

        // When
        String keyWithPrefix = mergePrefixAndKey(mockPrefix, mockKey);
        Mockito.when(stringRedisTemplate.delete(keyWithPrefix))
                .thenReturn(Boolean.TRUE);

        // Then
        cacheClientRedis.remove(mockPrefix, mockKey);

        // Verify
        Mockito.verify(stringRedisTemplate, Mockito.never())
                .opsForValue();

        Mockito.verify(stringRedisTemplate, Mockito.times(1))
                .delete(keyWithPrefix);
    }

    private static String mergePrefixAndKey(String prefix, String key) {
        return prefix + ":" + key;
    }

}
