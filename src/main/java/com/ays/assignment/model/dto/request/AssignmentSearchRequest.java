package com.ays.assignment.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO class representing the request data for searching assignment with respect to user's location.
 * <p>
 * This class provides getters and setters for the latitude, and longitude fields.
 * It also includes a builder pattern implementation for constructing instances of this class with optional parameters.
 * <p>
 * The purpose of this class is to encapsulate the request data related to searching assignment with respect to user's location, allowing for easy
 * transfer of the data between different layers of the application.
 */
@Getter
@Setter
public class AssignmentSearchRequest {

    @NotNull
    @Min(value = -180)
    @Max(value = 180)
    private Double longitude;

    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    private Double latitude;

}
