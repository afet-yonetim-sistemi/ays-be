package org.ays.assignment.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.assignment.model.enums.AssignmentStatus;

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
