package com.ays.assignment.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO class representing the request data for saving an assignment.
 * <p>
 * This class provides getters and setters for the description, first name, last name, latitude, longitude  and phone number fields.
 * It also includes a builder pattern implementation for constructing instances of this class with optional parameters.
 * <p>
 * The purpose of this class is to encapsulate the request data related to saving an assignment, allowing for easy
 * transfer of the data between different layers of the application.
 */
@Getter
@Setter
@Builder
public class AssignmentSaveRequest {

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
    private Double longitude;

    @NotNull
    private Double latitude;

}
