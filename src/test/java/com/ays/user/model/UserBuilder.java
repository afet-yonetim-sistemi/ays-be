package com.ays.user.model;

import com.ays.admin_user.controller.dto.request.AdminUserRegisterRequest;
import com.ays.common.model.TestDataBuilder;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * This is a class that can be used to generate test data for the UserDTO class without the need for manual input.
 */
public class UserBuilder extends TestDataBuilder<User> {

    public UserBuilder() {
        super(User.class);
    }

    /**
     * To change the status field inside the UserDTO object according to our preference, this method can be used.
     */
    public UserBuilder withStatus(UserStatus status) {
        data.setStatus(status);
        return this;
    }

    public UserBuilder from(User user) {
        data.setUsername(user.getUsername());
        data.setFirstName(user.getFirstName());
        data.setLastName(user.getLastName());
        data.setEmail(user.getEmail());
        data.setOrganization(user.getOrganization());
        data.setRole(user.getRole());
        data.setCountryCode(user.getCountryCode());
        data.setLineNumber(user.getLineNumber());
        data.setLastLoginDate(user.getLastLoginDate());
        return this;
    }

    public List<User> getUsers() {

        User userDTO1Info = User.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .role(UserRole.VOLUNTEER)
                .build();

        User userDTO2Info = User.builder()
                .username("username 2")
                .firstName("firstname 2")
                .lastName("lastname 2")
                .role(UserRole.VOLUNTEER)
                .build();

        User userDTO1 = new UserBuilder()
                .from(userDTO1Info)
                .build();

        User userDTO2 = new UserBuilder()
                .from(userDTO2Info)
                .build();

        return Arrays.asList(userDTO1, userDTO2);
    }

    public User getUserDTOwithPassiveStatus() {

        return User.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .role(UserRole.VOLUNTEER)
                .status(UserStatus.PASSIVE)
                .build();
    }

    public User getUpdatedUserDTO() {

        return User.builder()
                .username("updatedusername")
                .firstName("updatedfirstname")
                .lastName("updatedlastname")
                .role(UserRole.VOLUNTEER)
                .status(UserStatus.VERIFIED)
                .build();
    }

    public UserBuilder withRegisterRequest(AdminUserRegisterRequest registerRequest) {
        data.setUsername(registerRequest.getUsername());
        data.setPassword(registerRequest.getPassword());
        data.setFirstName(registerRequest.getFirstName());
        data.setLastName(registerRequest.getLastName());
        data.setRole(UserRole.ROLE_ADMIN);
        data.setCountryCode(registerRequest.getCountryCode());
        data.setLineNumber(registerRequest.getLineNumber());
        data.setEmail(registerRequest.getEmail());
        data.setLastLoginDate(LocalDateTime.now());
        return this;
    }
}
