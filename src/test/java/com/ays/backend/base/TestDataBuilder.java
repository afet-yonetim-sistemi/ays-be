package com.ays.backend.base;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
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

    private static final PositiveIntegerRandomizer positiveIntegerRandomizer = new PositiveIntegerRandomizer();
    private static final LongRangeRandomizer positiveLongRandomizer = new LongRangeRandomizer(1L, Long.MAX_VALUE);
    private static final CharacterRandomizer characterRandomizer = new CharacterRandomizer();


    protected final EasyRandom generator;
    protected T data;
    protected Class<T> clazz;

    public TestDataBuilder(Class<T> clazz) {
        this(clazz, false);
    }

    public TestDataBuilder(Class<T> clazz, boolean excludeRelations) {
        generator = new EasyRandom(getExclusionParameters(excludeRelations));

        this.clazz = clazz;
        data = generator.nextObject(clazz);
    }

    private EasyRandomParameters getExclusionParameters(boolean excludeRelations) {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.randomize(Integer.class, positiveIntegerRandomizer);
        parameters.randomize(String.class, characterRandomizer);
        parameters.randomize(Long.class, positiveLongRandomizer);

        if (!excludeRelations) return parameters;

        parameters.excludeField(
                FieldPredicates.isAnnotatedWith(ManyToOne.class, OneToMany.class, OneToOne.class, ManyToOne.class)
                        .or(named("id"))
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

