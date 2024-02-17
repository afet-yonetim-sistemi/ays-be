package org.ays.assignment.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.common.model.AysPhoneNumber;

/**
 * A DTO (Data Transfer Object) representing an assignment that is assigned to a user.
 */
@Getter
@Setter
@SuperBuilder
public class AssignmentUserResponse {

    private String id;
    private String description;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private AssignmentStatus status;
    private Location location;

    @Getter
    @Builder
    public static class Location {
        private Double longitude;
        private Double latitude;
    }

}
