package org.ays.admin_user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.admin_user.service.AdminUserAuthService;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.mapper.AysTokenToAysTokenResponseMapper;
import org.ays.auth.model.request.AysLoginRequest;
import org.ays.auth.model.request.AysTokenInvalidateRequest;
import org.ays.auth.model.request.AysTokenRefreshRequest;
import org.ays.auth.model.response.AysTokenResponse;
import org.ays.common.model.response.AysResponse;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Deprecated(since = "AdminUserAuthController V2 Production'a alınınca burası silinecektir.", forRemoval = true)
class AdminUserAuthController {

    private final AdminUserAuthService adminUserAuthService;
    private final AysTokenToAysTokenResponseMapper aysTokenToAysTokenResponseMapper = AysTokenToAysTokenResponseMapper.initialize();

    /**
     * This endpoint allows admin to login to platform.
     *
     * @param loginRequest A AysLoginRequest object required to login to platform.
     * @return A AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token")
    public AysResponse<AysTokenResponse> authenticate(@RequestBody @Valid AysLoginRequest loginRequest) {

        final AysToken aysToken = adminUserAuthService.authenticate(loginRequest);

        final AysTokenResponse aysTokenResponse = aysTokenToAysTokenResponseMapper.map(aysToken);

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

        final AysTokenResponse aysTokenResponse = aysTokenToAysTokenResponseMapper.map(aysToken);

        return AysResponse.successOf(aysTokenResponse);
    }

    /**
     * Endpoint for invalidating tokens. Only users with the 'ADMIN' authority are allowed to access this endpoint.
     * It invalidates the access token and refresh token associated with the provided refresh token.
     *
     * @param invalidateRequest the request object containing the refresh token to invalidate
     * @return AysResponse with a success message and no data
     */
    @PostMapping("/token/invalidate")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public AysResponse<Void> invalidateTokens(@RequestBody @Valid AysTokenInvalidateRequest invalidateRequest) {
        adminUserAuthService.invalidateTokens(invalidateRequest.getRefreshToken());
        return AysResponse.SUCCESS;
    }

}
