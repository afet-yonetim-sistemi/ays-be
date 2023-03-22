package com.ays.backend.user.controller;

import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.controller.payload.response.MessageResponse;
import com.ays.backend.user.security.JwtTokenProvider;
import com.ays.backend.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;


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

}
