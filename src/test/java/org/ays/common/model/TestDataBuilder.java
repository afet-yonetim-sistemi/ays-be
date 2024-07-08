package org.ays.common.model;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import static org.jeasy.random.FieldPredicates.named;

/**
 * This class is used to generate test data.
 * After creating a test class on the main class, this class can be used by extending it.
 */
public abstract class TestDataBuilder<T> {

    protected final EasyRandom generator;
    protected T data;
    protected Class<T> clazz;

    private static final PositiveIntegerRandomizer POSITIVE_INTEGER_RANDOMIZER = new PositiveIntegerRandomizer();
    private static final LongRangeRandomizer LONG_RANGE_RANDOMIZER = new LongRangeRandomizer(1L, Long.MAX_VALUE);
    private static final CharacterRandomizer CHARACTER_RANDOMIZER = new CharacterRandomizer();

    public TestDataBuilder(Class<T> clazz) {
        this.clazz = clazz;
        this.generator = new EasyRandom(this.getExclusionParameters());
        this.data = generator.nextObject(clazz);
    }

    private EasyRandomParameters getExclusionParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.randomize(Integer.class, POSITIVE_INTEGER_RANDOMIZER);
        parameters.randomize(String.class, CHARACTER_RANDOMIZER);
        parameters.randomize(Long.class, LONG_RANGE_RANDOMIZER);

        parameters.excludeField(
                named("createdUser")
                        .or(named("createdAt"))
                        .or(named("updatedUser"))
                        .or(named("updatedAt"))
        );
        return parameters;
    }

    public T build() {
        return data;
    }

}

class PositiveIntegerRandomizer extends IntegerRangeRandomizer {

    private static final int MIN = 0;
    private static final int MAX = 100;

    public PositiveIntegerRandomizer() {
        super(MIN, MAX);
    }

    @Override
    protected Integer getDefaultMinValue() {
        return MIN;
    }
}

class CharacterRandomizer extends StringRandomizer {

    @Override
    public String getRandomValue() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
