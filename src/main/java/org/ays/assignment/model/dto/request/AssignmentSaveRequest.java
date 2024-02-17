package org.ays.assignment.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.Name;

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
public class AssignmentSaveRequest {

    @NotBlank
    @Size(min = 2, max = 2048)
    private String description;

    @NotBlank
    @Name
    private String firstName;

    @NotBlank
    @Name
    private String lastName;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

    @NotNull
    @Min(value = -180)
    @Max(value = 180)
    private Double longitude;

    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    private Double latitude;

}
