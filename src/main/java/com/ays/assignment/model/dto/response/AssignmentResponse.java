package com.ays.assignment.model.dto.response;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.dto.response.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * A DTO (Data Transfer Object) representing a list of user assignment in a paginated response.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
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
    @Setter
    @Builder
    public static class Location {
        private Double longitude;
        private Double latitude;

    }

}
