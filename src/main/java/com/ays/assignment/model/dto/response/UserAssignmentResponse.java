package com.ays.assignment.model.dto.response;

import com.ays.common.model.dto.response.BaseResponse;
import com.ays.user.model.dto.response.UserResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * A DTO (Data Transfer Object) representing a list of assignment in a paginated response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserAssignmentResponse extends BaseResponse {

    private String id;
    private String description;
    private String firstName;
    private String lastName;
    private Double longitude;
    private Double latitude;

    private UserResponse user;

}
