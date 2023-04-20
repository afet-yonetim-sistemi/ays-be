package com.ays.user.controller;

import com.ays.auth.model.AysToken;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.dto.request.AysTokenRefreshRequest;
import com.ays.auth.model.dto.response.AysTokenResponse;
import com.ays.common.controller.dto.response.AysResponse;
import com.ays.user.service.UserAuthService;
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
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
class UserAuthController {

    private final UserAuthService userAuthService;


    /**
     * This endpoint allows user to login to platform.
     *
     * @param loginRequest A AysLoginRequest object required to login to platform.
     * @return A AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token")
    public AysResponse<AysTokenResponse> authenticate(@RequestBody @Valid AysLoginRequest loginRequest) {

        final AysToken aysToken = userAuthService.authenticate(loginRequest);

        AysTokenResponse authResponse = AysTokenResponse.builder()
                .accessToken(aysToken.getAccessToken())
                .accessTokenExpiresAt(aysToken.getAccessTokenExpiresAt())
                .refreshToken(aysToken.getRefreshToken())
                .build();

        return AysResponse.successOf(authResponse);
    }

    /**
     * This endpoint allows user to refresh token.
     *
     * @param refreshRequest A AysTokenRefreshRequest object used to send a token.
     * @return A AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token/refresh")
    public AysResponse<AysTokenResponse> refreshToken(@RequestBody @Valid AysTokenRefreshRequest refreshRequest) {

        final AysToken aysToken = userAuthService.refreshAccessToken(refreshRequest.getRefreshToken());

        AysTokenResponse aysTokenResponse = AysTokenResponse.builder()
                .accessToken(aysToken.getAccessToken())
                .accessTokenExpiresAt(aysToken.getAccessTokenExpiresAt())
                .refreshToken(aysToken.getRefreshToken())
                .build();

        return AysResponse.successOf(aysTokenResponse);
    }

}
