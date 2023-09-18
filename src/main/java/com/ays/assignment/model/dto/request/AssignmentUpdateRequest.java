package com.ays.assignment.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request object for updating an assignment.
 * This class contains information such as description, first name, last name,
 * phone number, latitude, and longitude for the assignment.
 */
@Getter
@Setter
public class AssignmentUpdateRequest {

    @NotBlank
    private String description;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Valid
    @NotNull
    private AysPhoneNumber phoneNumber;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

}
