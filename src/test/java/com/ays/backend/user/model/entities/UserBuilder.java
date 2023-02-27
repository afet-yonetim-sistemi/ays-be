package com.ays.backend.user.model.entities;

import com.ays.backend.base.TestDataBuilder;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.service.dto.UserDTO;
import com.ays.backend.user.service.dto.UserDTOBuilder;

/**
 * This is a class that can be used to generate test data for the User class without the need for manual input.
 */
public class UserBuilder extends TestDataBuilder<User> {

    public UserBuilder() {
        super(User.class);
    }

    /**
     * It is used to create a UserBuilder object with the data from the SignUpRequest object.
     */
    public UserBuilder withSignUpRequest(SignUpRequest signUpRequest) {
        data.setUsername(signUpRequest.getUsername());
        data.setPassword(signUpRequest.getPassword());
        data.setStatus(UserStatus.getById(signUpRequest.getStatusId()));
        data.setCountryCode(signUpRequest.getCountryCode());
        data.setLineNumber(signUpRequest.getLineNumber());
        return this;
    }

    /**
     * To change the userRole field inside the User object according to our preference, this method can be used.
     */
    public UserBuilder withUserRole(UserRole userRole) {
        data.setUserRole(userRole);
        return this;
    }

    public UserBuilder withAllInfo(User user) {
        data.setUsername(user.getUsername());
        data.setFirstName(user.getFirstName());
        data.setLastName(user.getLastName());
        data.setEmail(user.getEmail());
        data.setOrganization(user.getOrganization());
        data.setUserRole(user.getUserRole());
        data.setCountryCode(user.getCountryCode());
        data.setLineNumber(user.getLineNumber());
        data.setLastLoginDate(user.getLastLoginDate());
        return this;
    }
}
