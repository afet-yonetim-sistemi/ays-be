package com.ays.backend.user.model.entities;

import com.ays.backend.base.TestDataBuilder;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;

public class UserBuilder extends TestDataBuilder<User> {

    public UserBuilder() {
        super(User.class);
    }

    public UserBuilder withSignUpRequest(SignUpRequest signUpRequest) {
        data.setUsername(signUpRequest.getUsername());
        data.setPassword(signUpRequest.getPassword());
        data.setStatus(UserStatus.getById(signUpRequest.getStatusId()));
        data.setCountryCode(signUpRequest.getCountryCode());
        data.setLineNumber(signUpRequest.getLineNumber());
        return this;
    }

    public UserBuilder withUserRole(UserRole userRole) {
        data.setUserRole(userRole);
        return this;
    }
}
