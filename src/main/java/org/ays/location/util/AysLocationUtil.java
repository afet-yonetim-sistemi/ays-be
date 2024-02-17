package org.ays.location.util;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
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
     * @param longitude The longitude coordinate of the location. The X coordinate, or Longitude in geospatial contexts.
     * @param latitude  The latitude coordinate of the location. The Y coordinate, or Latitude in geospatial contexts.
     * @return A Point object representing the location.
     */
    public static Point generatePoint(final Double longitude, final Double latitude) {
        final Coordinate[] coordinates = new Coordinate[]{new Coordinate(longitude, latitude)};
        final CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
        final PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
        return new GeometryFactory(precisionModel, 4326).createPoint(coordinateSequence);
    }
}
