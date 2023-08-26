package com.ays.assignment.model.dto.response;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.dto.response.BaseResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents the response for a user with assignment.
 * It includes information such as the user's username, first and last name, email, institution, role and status.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AssignmentSearchResponse extends BaseResponse {

    private String id;
    private String description;
    private Double longitude;
    private Double latitude;
    private AssignmentStatus status;

}
