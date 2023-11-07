package com.ays.assignment.model.dto.response;

import com.ays.assignment.model.enums.AssignmentStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO (Data Transfer Object) representing a summary of an assignment.
 */
@Getter
@Setter
public class AssignmentSummaryResponse {

    private String id;
    private String description;
    private AssignmentStatus status;
    private Location location;

    @Getter
    @Setter
    public static class Location {
        private Double longitude;
        private Double latitude;
    }
}
