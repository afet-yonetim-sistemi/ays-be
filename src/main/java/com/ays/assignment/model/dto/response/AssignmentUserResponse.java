package com.ays.assignment.model.dto.response;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.dto.response.BaseResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * A DTO (Data Transfer Object) representing an assignment that is assigned to a user.
 */
@Getter
@Setter
@SuperBuilder
public class AssignmentUserResponse extends BaseResponse {

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
