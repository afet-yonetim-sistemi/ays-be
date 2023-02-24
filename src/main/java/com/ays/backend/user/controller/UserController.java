package com.ays.backend.user.controller;

import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.controller.payload.response.SignUpResponse;
import com.ays.backend.user.exception.UserAlreadyExistsException;
import com.ays.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller to perform user related api operations.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<SignUpResponse> registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userService.existsByUsername(signUpRequest.getUsername()))) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }

        var createdUser = userService.saveUser(signUpRequest);

        SignUpResponse signUpResponse = new SignUpResponse(createdUser.getUsername());
        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }
}
