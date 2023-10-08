package com.ays.assignment.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

/**
 * Represents a response object for assignment search results.
 * <p>
 * This class provides information about an assignment, including its unique identifier, description, and location.
 */
@Getter
@Setter
@Builder
public class AssignmentSearchResponse {

    /**
     * The unique identifier of the assignment.
     */
    private String id;
    /**
     * A description of the assignment.
     */
    private String description;
    /**
     * The location of the assignment specified as longitude and latitude.
     */
    private Location location;

    /**
     * Represents the location information for an assignment.
     */
    @Getter
    @Builder
    public static class Location {
        /**
         * The longitude coordinate of the assignment location.
         */
        private Double longitude;
        /**
         * The latitude coordinate of the assignment location.
         */
        private Double latitude;
    }

    /**
     * Sets the location of the assignment based on a Point object.
     *
     * @param point The Point object containing longitude and latitude information.
     */
    public void setLocation(Point point) {
        this.location = Location.builder()
                .longitude(point.getX())
                .latitude(point.getY())
                .build();
    }

}
