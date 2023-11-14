package com.ays.assignment.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class representing the request data for canceling an assignment.
 */
@Getter
@Setter
public class AssignmentCancelRequest {
    @NotBlank
    @Size(min = 40, max = 512)
    private String reason;
}
