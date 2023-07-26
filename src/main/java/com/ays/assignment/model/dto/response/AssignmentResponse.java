package com.ays.assignment.model.dto.response;

import com.ays.common.model.dto.response.BaseResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
    private String firstName;
    private String lastName;
    private Double longitude;
    private Double latitude;

    private AssignmentSearchResponse assignmentSearchResponse;

}
