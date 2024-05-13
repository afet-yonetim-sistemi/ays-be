package org.ays.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.dto.request.AysLoginRequestV2;
import org.ays.auth.model.dto.request.AysTokenInvalidateRequest;
import org.ays.auth.model.dto.request.AysTokenRefreshRequest;
import org.ays.auth.model.dto.response.AysTokenResponse;
import org.ays.auth.model.mapper.AysTokenToAysTokenResponseMapper;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.user.service.UserAuthServiceV2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth controller to perform authentication api operations.
 */
@RestController
@RequestMapping("/api/v2/authentication")
@RequiredArgsConstructor
class UserAuthControllerV2 {

    private final UserAuthServiceV2 userAuthService;

    private final AysTokenToAysTokenResponseMapper aysTokenToAysTokenResponseMapper = AysTokenToAysTokenResponseMapper.initialize();


    /**
     * This endpoint allows user to login to platform.
     *
     * @param loginRequest An AysLoginRequest object required to login to platform.
     * @return An AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token")
    public AysResponse<AysTokenResponse> landingAuthenticate(@RequestBody @Valid AysLoginRequestV2 loginRequest) {

        final AysToken aysToken = userAuthService.authenticate(loginRequest);

        final AysTokenResponse aysTokenResponse = aysTokenToAysTokenResponseMapper.map(aysToken);

        return AysResponse.successOf(aysTokenResponse);
    }

    /**
     * This endpoint allows user to refresh token.
     *
     * @param refreshRequest An AysTokenRefreshRequest object used to send a token.
     * @return An AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token/refresh")
    public AysResponse<AysTokenResponse> refreshToken(@RequestBody @Valid AysTokenRefreshRequest refreshRequest) {

        final AysToken aysToken = userAuthService.refreshAccessToken(refreshRequest.getRefreshToken());

        final AysTokenResponse aysTokenResponse = aysTokenToAysTokenResponseMapper.map(aysToken);

        return AysResponse.successOf(aysTokenResponse);
    }

    /**
     * Endpoint for invalidating a token. Only users with the 'USER' authority are allowed to access this endpoint.
     * It invalidates the access token and refresh token associated with the provided refresh token.
     *
     * @param invalidateRequest the request object containing the refresh token to invalidate
     * @return AysResponse with a success message and no data
     */
    @PostMapping("/token/invalidate")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<Void> invalidateTokens(@RequestBody @Valid AysTokenInvalidateRequest invalidateRequest) {
        userAuthService.invalidateTokens(invalidateRequest.getRefreshToken());
        return AysResponse.SUCCESS;
    }

}
