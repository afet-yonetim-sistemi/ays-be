package com.ays.assignment.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.util.validation.Name;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private AysPhoneNumber phoneNumber;

    @NotNull
    @Min(value = -180)
    @Max(value = 180)
    private Double longitude;

    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    private Double latitude;

}
