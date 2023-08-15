package com.ays.assignment.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import lombok.Builder;
import lombok.Data;

/**
 * A DTO class representing the request data for saving an assignment.
 * <p>
 * This class provides getters and setters for the description, first name, last name, latitude, longitude  and phone number fields.
 * It also includes a builder pattern implementation for constructing instances of this class with optional parameters.
 * <p>
 * The purpose of this class is to encapsulate the request data related to saving an assignment, allowing for easy
 * transfer of the data between different layers of the application.
 */
@Data
@Builder
public class AssignmentSaveRequest {

    private String description;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private double latitude;
    private double longitude;
}
