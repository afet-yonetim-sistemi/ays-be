package org.ays.auth.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.Name;

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

    @NotBlank
    @Name
    private String firstName;

    @NotBlank
    @Name
    private String lastName;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

}
