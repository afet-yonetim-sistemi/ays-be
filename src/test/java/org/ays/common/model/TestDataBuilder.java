package org.ays.common.model;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.AbstractRandomizer;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
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

    private static final PointRandomizer POINT_RANDOMIZER = new PointRandomizer();


    public TestDataBuilder(Class<T> clazz) {
        this(clazz, false);
    }

    public TestDataBuilder(Class<T> clazz, boolean excludeRelations) {
        this.clazz = clazz;
        this.generator = new EasyRandom(this.getExclusionParameters(excludeRelations));
        this.data = generator.nextObject(clazz);
    }

    public T build() {
        return data;
    }

    private EasyRandomParameters getExclusionParameters(boolean excludeRelations) {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.randomize(Integer.class, POSITIVE_INTEGER_RANDOMIZER);
        parameters.randomize(String.class, CHARACTER_RANDOMIZER);
        parameters.randomize(Long.class, LONG_RANGE_RANDOMIZER);
        parameters.randomize(Point.class, POINT_RANDOMIZER);

        if (!excludeRelations) return parameters;

        parameters.excludeField(
                FieldPredicates.isAnnotatedWith(ManyToOne.class, OneToMany.class, OneToOne.class)
                        .or(named("id"))
        );
        return parameters;
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


class PointRandomizer extends AbstractRandomizer<Point> {

    @Override
    public Point getRandomValue() {
        Coordinate[] coordinates = new Coordinate[]{new Coordinate(40.991739, 29.024168)};
        CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(coordinateSequence);
    }
}

