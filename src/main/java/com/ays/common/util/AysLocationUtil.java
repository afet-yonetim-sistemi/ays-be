package com.ays.common.util;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

/**
 * Utility class for handling location-related operations.
 * Provides methods to generate geometric points based on latitude and longitude coordinates.
 */
@UtilityClass
public class AysLocationUtil {

    /**
     * Generates a Point object representing a location based on the provided latitude and longitude coordinates.
     *
     * @param latitude  The latitude coordinate of the location.
     * @param longitude The longitude coordinate of the location.
     * @return A Point object representing the location.
     */
    public static Point generatePoint(final Double latitude, final Double longitude) {
        final Coordinate[] coordinates = new Coordinate[]{new Coordinate(latitude, longitude)};
        final CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
        final PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
        return new GeometryFactory(precisionModel, 4326).createPoint(coordinateSequence);
    }

}
