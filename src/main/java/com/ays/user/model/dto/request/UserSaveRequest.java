package com.ays.user.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.util.validation.Name;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO class representing the request data for saving a user.
 * <p>
 * This class provides getters and setters for the first name, last name, and phone number fields.
 * It also includes a builder pattern implementation for constructing instances of this class with optional parameters.
 * <p>
 * The purpose of this class is to encapsulate the request data related to saving a user, allowing for easy
 * transfer of the data between different layers of the application.
 */
@Getter
@Setter
public class UserSaveRequest {

    @NotNull
    @Name
    private String firstName;

    @NotNull
    @Name
    private String lastName;

    @Valid
    @NotNull
    private AysPhoneNumber phoneNumber;

}
