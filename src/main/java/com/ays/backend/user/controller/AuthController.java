package com.ays.backend.user.controller;

import com.ays.backend.user.controller.payload.request.RegisterRequest;
import com.ays.backend.user.controller.payload.response.AuthResponse;
import com.ays.backend.user.exception.UserAlreadyExistsException;
import com.ays.backend.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth controller to perform authentication api operations.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * This endpoint allows admin to register to platform.
     *
     * @param registerRequest A RegisterRequest object required to register to platform .
     * @return A ResponseEntity containing a AuthResponse object with the access token and success message of the newly created admin and
     *         the HTTP status code (201 CREATED).
     * @throws UserAlreadyExistsException If the username provided in the request body already exists in the database.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {

        if (Boolean.TRUE.equals(authService.existsByUsername(registerRequest.getUsername()))) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }

        return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
    }

}
