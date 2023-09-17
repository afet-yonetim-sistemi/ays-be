package com.ays.assignment.model.dto.response;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.dto.response.BaseResponse;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

/**
 * This class represents the response for a user with assignment.
 * It includes information such as the user's username, first and last name, email, institution, role and status.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AssignmentSearchResponse extends BaseResponse {

    private String id;
    private String description;
    private Location location;
    private AssignmentStatus status;

    @Getter
    @Builder
    public static class Location {
        private Double longitude;
        private Double latitude;
    }

    public void setLocation(Point point) {
        this.location = Location.builder()
                .longitude(point.getX())
                .latitude(point.getY())
                .build();
    }

}
