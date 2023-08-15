package com.ays.assignment.model.dto.request;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPhoneNumber;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a request object for updating an assignment with its variables.
 * <p>
 * This class provides getters and setters for the description, first name, last name, latitude, longitude  and phone number fields.
 * It also includes a builder pattern implementation for constructing instances of this class with optional parameters.
 * <p>
 * The purpose of this class is to encapsulate the request data related to updating a user, allowing for easy
 * transfer of the data between different layers of the application.
 */
@Data
@Builder
public class AssignmentUpdateRequest {

    private String description;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private double latitude;
    private double longitude;
    private AssignmentStatus status;
}
