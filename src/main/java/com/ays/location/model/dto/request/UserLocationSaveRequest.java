package com.ays.location.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO class representing the request data for saving/updating user location.
 * <p>
 * This class provides getters and setters for the latitude, and longitude fields.
 * It also includes a builder pattern implementation for constructing instances of this class with optional parameters.
 * <p>
 * The purpose of this class is to encapsulate the request data related to updating user location, allowing for easy
 * transfer of the data between different layers of the application.
 */
@Getter
@Setter
public class UserLocationSaveRequest {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

}
