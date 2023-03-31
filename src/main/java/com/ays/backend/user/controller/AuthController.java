package com.ays.backend.user.controller;

import com.ays.backend.user.controller.payload.request.AdminLoginRequest;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.controller.payload.response.AuthResponse;
import com.ays.backend.user.controller.payload.response.MessageResponse;
import com.ays.backend.user.model.Token;
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
     * @param registerRequest A AdminRegisterRequest object required to register to platform .
     * @return A ResponseEntity containing a MessageResponse object with success message of the newly created admin and
     * the HTTP status code (201 CREATED).
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody AdminRegisterRequest registerRequest) {

        // TODO : return type registeredUser unused temporarily now, perhaps delete it later if it is not used.
        var registeredUser = authService.register(registerRequest);

        MessageResponse messageResponse = new MessageResponse("success");

        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }


    /**
     * This endpoint allows admin to login to platform.
     *
     * @param loginRequest A AdminLoginRequest object required to register to platform .
     * @return A ResponseEntity containing an AuthResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AdminLoginRequest loginRequest) {

        final var aysToken = authService.login(loginRequest);

        AuthResponse authResponse = AuthResponse.builder()
                .accessTokenExpireIn(aysToken.getAccessTokenExpireIn())
                .refreshToken(aysToken.getRefreshToken())
                .accessToken(aysToken.getAccessToken())
                .build();

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    /**
     * This endpoint allows admin to refresh token.
     *
     * @param refreshToken A refreshToken required to refresh to accesstoken for login process .
     * @return A ResponseEntity containing an AuthResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody String refreshToken) {

        final var renewToken = authService.refreshToken(refreshToken);

        AuthResponse authResponse = AuthResponse.builder()
                .accessTokenExpireIn(renewToken.getAccessTokenExpireIn())
                .refreshToken(renewToken.getRefreshToken())
                .accessToken(renewToken.getAccessToken())
                .build();

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


}
