package org.ays.assignment.model.dto.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.dto.response.BaseResponse;

/**
 * A DTO (Data Transfer Object) representing a list of user assignment in a paginated response.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AssignmentResponse extends BaseResponse {

    private String id;
    private String description;
    private AssignmentStatus status;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private Location location;
    private User user;

    @Getter
    @Setter
    @Builder
    public static class User {

        private String id;
        private String firstName;
        private String lastName;
        private AysPhoneNumber phoneNumber;
        private Location location;

    }

    @Getter
    @Builder
    public static class Location {
        private Double longitude;
        private Double latitude;

    }

}
