package com.ays.admin_user.controller;

import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.service.AdminUserAuthService;
import com.ays.auth.controller.dto.request.AysLoginRequest;
import com.ays.auth.controller.dto.request.AysTokenRefreshRequest;
import com.ays.auth.controller.dto.response.AysTokenResponse;
import com.ays.auth.model.AysToken;
import com.ays.common.controller.dto.response.AysResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth controller to perform authentication api operations.
 */
@RestController
@RequestMapping("/api/v1/authentication/admin")
@RequiredArgsConstructor
public class AdminUserAuthController {

    private final AdminUserAuthService adminUserAuthService;

    /**
     * This endpoint allows admin to register to platform.
     *
     * @param registerRequest A AdminRegisterRequest object required to register to platform.
     * @return A AysResponse containing a Void object with success message of the newly created admin and
     * the HTTP status code (201 CREATED).
     */
    @PostMapping("/register")
    public AysResponse<Void> register(@RequestBody @Valid AdminUserRegisterRequest registerRequest) {
        adminUserAuthService.register(registerRequest);
        return AysResponse.SUCCESS;
    }


    /**
     * This endpoint allows admin to login to platform.
     *
     * @param loginRequest A AysLoginRequest object required to login to platform.
     * @return A AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token")
    public AysResponse<AysTokenResponse> authenticate(@RequestBody @Valid AysLoginRequest loginRequest) {

        final AysToken aysToken = adminUserAuthService.authenticate(loginRequest);

        AysTokenResponse aysTokenResponse = AysTokenResponse.builder()
                .accessToken(aysToken.getAccessToken())
                .accessTokenExpiresAt(aysToken.getAccessTokenExpiresAt())
                .refreshToken(aysToken.getRefreshToken())
                .build();

        return AysResponse.successOf(aysTokenResponse);
    }

    /**
     * This endpoint allows admin to refresh token.
     *
     * @param refreshRequest A AysTokenRefreshRequest object used to send a token.
     * @return A AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token/refresh")
    public AysResponse<AysTokenResponse> refreshToken(@RequestBody @Valid AysTokenRefreshRequest refreshRequest) {

        final AysToken aysToken = adminUserAuthService.refreshAccessToken(refreshRequest.getRefreshToken());

        AysTokenResponse aysTokenResponse = AysTokenResponse.builder()
                .accessToken(aysToken.getAccessToken())
                .accessTokenExpiresAt(aysToken.getAccessTokenExpiresAt())
                .refreshToken(aysToken.getRefreshToken())
                .build();

        return AysResponse.successOf(aysTokenResponse);
    }


}
