package com.ays.user.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO class representing the response data returned when a user is saved.
 * <p>
 * This class provides getters and setters for the username and password fields.
 * It also includes a builder pattern implementation for constructing instances of this class with optional parameters.
 * <p>
 * The purpose of this class is to encapsulate the response data related to saving a user, allowing for easy
 * transfer of the data between different layers of the application.
 */
@Getter
@Setter
@Builder
public class UserSavedResponse {

    /**
     * This field is created by the application.
     */
    private String username;

    /**
     * This field is created by the application.
     */
    private String password;

}
