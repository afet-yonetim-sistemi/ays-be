package com.ays.user.model.entity;

import com.ays.admin_user.controller.dto.request.AdminUserRegisterRequest;
import com.ays.common.model.TestDataBuilder;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * This is a class that can be used to generate test data for the User class without the need for manual input.
 */
public class UserEntityBuilder extends TestDataBuilder<UserEntity> {

    public UserEntityBuilder() {
        super(UserEntity.class);
    }

    /**
     * It is used to create a UserBuilder object with the data from the SignUpRequest object.
     */
    public UserEntityBuilder withSignUpRequest(AdminUserRegisterRequest registerRequest) {
        data.setUsername(registerRequest.getUsername());
        data.setPassword(registerRequest.getPassword());
        data.setCountryCode(registerRequest.getCountryCode());
        data.setLineNumber(registerRequest.getLineNumber());
        return this;
    }

    /**
     * To change the userRole field inside the User object according to our preference, this method can be used.
     */
    public UserEntityBuilder withUserRole(UserRole userRole) {
        data.setRole(userRole);
        return this;
    }

    public UserEntityBuilder withUserStatus(UserStatus userStatus) {
        data.setStatus(userStatus);
        return this;
    }

    public UserEntityBuilder from(UserEntity user) {
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


    public List<UserEntity> getUsers() {

        UserEntity userInfo1 = UserEntity.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .role(UserRole.VOLUNTEER)
                .build();

        UserEntity userInfo2 = UserEntity.builder()
                .username("username 2")
                .firstName("firstname 2")
                .lastName("lastname 2")
                .role(UserRole.VOLUNTEER)
                .build();

        UserEntity user1 = new UserEntityBuilder()
                .from(userInfo1)
                .build();

        UserEntity user2 = new UserEntityBuilder()
                .from(userInfo2)
                .build();

        return Arrays.asList(user1, user2);
    }


    public UserEntity getUserSamplewithWaitingStatus() {

        return UserEntity.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .role(UserRole.VOLUNTEER)
                .status(UserStatus.WAITING)
                .build();

    }

    public UserEntity getUserSamplewithPassiveStatus() {

        return UserEntity.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .role(UserRole.VOLUNTEER)
                .status(UserStatus.PASSIVE)
                .build();

    }

    public UserEntity getUserSample() {

        return UserEntity.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .role(UserRole.VOLUNTEER)
                .status(UserStatus.COMPLETED)
                .build();

    }

    public UserEntity getUpdatedUser() {

        return UserEntity.builder()
                .username("updatedusername")
                .firstName("updatedfirstname")
                .lastName("updatedlastname")
                .role(UserRole.VOLUNTEER)
                .status(UserStatus.VERIFIED)
                .build();
    }

    /**
     * It is used to create a UserBuilder object with the data from the AdminRegisterRequest object.
     */
    public UserEntityBuilder withRegisterRequest(AdminUserRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
        data.setUsername(registerRequest.getUsername());
        data.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
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
