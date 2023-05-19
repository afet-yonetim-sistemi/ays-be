package com.ays.admin_user.controller;

import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.service.AdminUserAuthService;
import com.ays.admin_user.service.AdminUserRegisterService;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.dto.request.AysTokenInvalidateRequest;
import com.ays.auth.model.dto.request.AysTokenRefreshRequest;
import com.ays.auth.model.dto.response.AysTokenResponse;
import com.ays.auth.model.mapper.AysTokenToAysTokenResponseMapper;
import com.ays.common.model.dto.response.AysResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
class AdminUserAuthController {

    private final AdminUserAuthService adminUserAuthService;
    private final AdminUserRegisterService adminUserRegisterService;
    private final AysTokenToAysTokenResponseMapper aysTokenToAysTokenResponseMapper = AysTokenToAysTokenResponseMapper.initialize();

    /**
     * This endpoint allows admin to register to platform.
     *
     * @param registerRequest A AdminRegisterRequest object required to register to platform.
     * @return A AysResponse containing a Void object with success message of the newly created admin and
     * the HTTP status code (201 CREATED).
     */
    @PostMapping("/register")
    public AysResponse<Void> register(@RequestBody @Valid AdminUserRegisterRequest registerRequest) {
        adminUserRegisterService.register(registerRequest);
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

    @PostMapping("/token/invalidate")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<Void> invalidateTokens(@RequestBody @Valid AysTokenInvalidateRequest invalidateRequest) {
        adminUserAuthService.invalidateTokens(invalidateRequest.getRefreshToken());
        return AysResponse.SUCCESS;
    }

}
