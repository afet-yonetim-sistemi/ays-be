package com.ays.backend.user.model.entities;

import com.ays.backend.base.TestDataBuilder;
import com.ays.backend.user.controller.payload.request.AdminLoginRequest;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

    public UserBuilder withUserStatus(UserStatus userStatus) {
        data.setStatus(userStatus);
        return this;
    }

    public UserBuilder from(User user) {
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


    public List<User> getUsers(){

        User userInfo1 = User.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .userRole(UserRole.ROLE_VOLUNTEER)
                .build();

        User userInfo2 = User.builder()
                .username("username 2")
                .firstName("firstname 2")
                .lastName("lastname 2")
                .userRole(UserRole.ROLE_VOLUNTEER)
                .build();

        User user1 = new UserBuilder()
                .from(userInfo1)
                .build();

        User user2 = new UserBuilder()
                .from(userInfo2)
                .build();

        return Arrays.asList(user1, user2);
    }


    public User getUserSamplewithWaitingStatus(){

        return User.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .userRole(UserRole.ROLE_VOLUNTEER)
                .status(UserStatus.WAITING)
                .build();

    }

    public User getUserSamplewithPassiveStatus(){

        return User.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .userRole(UserRole.ROLE_VOLUNTEER)
                .status(UserStatus.PASSIVE)
                .build();

    }

    public User getUserSample(){

        return User.builder()
                .username("username 1")
                .firstName("firstname 1")
                .lastName("lastname 1")
                .userRole(UserRole.ROLE_VOLUNTEER)
                .status(UserStatus.COMPLETED)
                .build();

    }

    public User getUpdatedUser(){

        return User.builder()
                .username("updatedusername")
                .firstName("updatedfirstname")
                .lastName("updatedlastname")
                .userRole(UserRole.ROLE_VOLUNTEER)
                .status(UserStatus.VERIFIED)
                .build();
    }

    public AdminRegisterRequest getRegisterRequest() {
        return AdminRegisterRequest.builder()
                .username("testadmin")
                .password("testadmin")
                .countryCode(1)
                .lineNumber(1234567890)
                .firstName("First Name Admin")
                .lastName("Last Name Admin")
                .email("testadmin@afet.com")
                //.organizationId(1L)
                .statusValue(1)
                .build();
    }

    /**
     * It is used to create a UserBuilder object with the data from the AdminRegisterRequest object.
     */
    public UserBuilder withRegisterRequest(AdminRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
        data.setUsername(registerRequest.getUsername());
        data.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        data.setFirstName(registerRequest.getFirstName());
        data.setLastName(registerRequest.getLastName());
        data.setUserRole(UserRole.ROLE_ADMIN);
        data.setCountryCode(registerRequest.getCountryCode());
        data.setLineNumber(registerRequest.getLineNumber());
        data.setEmail(registerRequest.getEmail());
        data.setLastLoginDate(LocalDateTime.now());
        return this;
    }


    public AdminLoginRequest getLoginRequest() {
        return AdminLoginRequest.builder()
                .username("adminUsername")
                .password("adminPassword")
                .build();
    }
}
