package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.mapper.AysTokenToAysTokenResponseMapper;
import org.ays.auth.model.request.AysLoginRequest;
import org.ays.auth.model.request.AysTokenInvalidateRequest;
import org.ays.auth.model.request.AysTokenRefreshRequest;
import org.ays.auth.model.response.AysTokenResponse;
import org.ays.auth.service.AysAuthService;
import org.ays.common.model.response.AysResponse;
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
class AysAuthController {

    private final AysAuthService userAuthService;


    private final AysTokenToAysTokenResponseMapper aysTokenToAysTokenResponseMapper = AysTokenToAysTokenResponseMapper.initialize();


    /**
     * This endpoint allows user to login to platform.
     *
     * @param loginRequest An AysLoginRequest object required to login to platform.
     * @return An AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token")
    public AysResponse<AysTokenResponse> landingAuthenticate(@RequestBody @Valid AysLoginRequest loginRequest) {
        final AysToken token = userAuthService.authenticate(loginRequest);
        final AysTokenResponse tokenResponse = aysTokenToAysTokenResponseMapper.map(token);
        return AysResponse.successOf(tokenResponse);
    }

    /**
     * This endpoint allows user to refresh token.
     *
     * @param refreshRequest An AysTokenRefreshRequest object used to send a token.
     * @return An AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token/refresh")
    public AysResponse<AysTokenResponse> refreshToken(@RequestBody @Valid AysTokenRefreshRequest refreshRequest) {
        final AysToken token = userAuthService.refreshAccessToken(refreshRequest.getRefreshToken());
        final AysTokenResponse tokenResponse = aysTokenToAysTokenResponseMapper.map(token);
        return AysResponse.successOf(tokenResponse);
    }

    /**
     * Endpoint for invalidating a token. Only users with the 'USER' authority are allowed to access this endpoint.
     * It invalidates the access token and refresh token associated with the provided refresh token.
     *
     * @param invalidateRequest the request object containing the refresh token to invalidate
     * @return AysResponse with a success message and no data
     */
    @PostMapping("/token/invalidate")
    public AysResponse<Void> invalidateTokens(@RequestBody @Valid AysTokenInvalidateRequest invalidateRequest) {
        userAuthService.invalidateTokens(invalidateRequest.getRefreshToken());
        return AysResponse.SUCCESS;
    }

}
