package com.ays.assignment.model.dto.request;

import lombok.Builder;
import lombok.Getter;

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
@Builder
public class AssignmentSearchRequest {

    private Double latitude;
    private Double longitude;
}