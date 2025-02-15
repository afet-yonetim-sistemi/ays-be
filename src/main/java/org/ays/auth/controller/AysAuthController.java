package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysToken;
import org.ays.auth.model.mapper.AysTokenToResponseMapper;
import org.ays.auth.model.request.AysLoginRequest;
import org.ays.auth.model.request.AysPasswordCreateRequest;
import org.ays.auth.model.request.AysPasswordForgotRequest;
import org.ays.auth.model.request.AysTokenInvalidateRequest;
import org.ays.auth.model.request.AysTokenRefreshRequest;
import org.ays.auth.model.response.AysTokenResponse;
import org.ays.auth.service.AysAuthService;
import org.ays.auth.service.AysUserPasswordService;
import org.ays.common.model.response.AysResponse;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth controller to perform authentication api operations.
 */
@Validated
@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
class AysAuthController {

    private final AysAuthService authService;
    private final AysUserPasswordService userPasswordService;


    private final AysTokenToResponseMapper tokenToTokenResponseMapper = AysTokenToResponseMapper.initialize();


    /**
     * This endpoint allows user to login to platform.
     *
     * @param loginRequest An AysLoginRequest object required to login to platform.
     * @return An AysResponse containing an AysTokenResponse object and the HTTP status code (200 OK).
     */
    @PostMapping("/token")
    public AysResponse<AysTokenResponse> authenticate(@RequestBody @Valid AysLoginRequest loginRequest) {
        final AysToken token = authService.authenticate(loginRequest);
        final AysTokenResponse tokenResponse = tokenToTokenResponseMapper.map(token);
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
        final AysToken token = authService.refreshAccessToken(refreshRequest.getRefreshToken());
        final AysTokenResponse tokenResponse = tokenToTokenResponseMapper.map(token);
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
        authService.invalidateTokens(invalidateRequest.getRefreshToken());
        return AysResponse.success();
    }


    /**
     * This endpoint allows a user to request a password create.
     *
     * @param forgotPasswordRequest An AysForgotPasswordRequest object containing the user's email address.
     * @return An AysResponse indicating the success of the password create request.
     */
    @PostMapping("/password/forgot")
    public AysResponse<Void> forgotPassword(@RequestBody @Valid AysPasswordForgotRequest forgotPasswordRequest) {
        userPasswordService.forgotPassword(forgotPasswordRequest);
        return AysResponse.success();
    }


    /**
     * Endpoint for checking the existence and validity of a password reset token.
     * <p>
     * This endpoint handles the verification of a password reset token by its ID.
     * It delegates the check to the user password service.
     * </p>
     *
     * @param id The ID of the password reset token to be checked.
     * @return AysResponse with a success message and no data.
     */
    @GetMapping("/password/{id}/validity")
    public AysResponse<Void> checkPasswordChangingValidity(@PathVariable @UUID String id) {
        userPasswordService.checkPasswordChangingValidity(id);
        return AysResponse.success();
    }


    /**
     * Handles the request to create a new password for a user.
     * <p>
     * This endpoint processes a request to set a new password for a user identified
     * by the provided password ID. It validates the request and updates the user's
     * password if the request meets the required criteria.
     *
     * @param id            The unique identifier of the user for whom the password is being created.
     * @param createRequest The request body containing the new password details.
     * @return A response indicating the success of the operation.
     */
    @PostMapping("/password/{id}")
    public AysResponse<Void> forgotPassword(@PathVariable @UUID String id,
                                            @RequestBody @Valid AysPasswordCreateRequest createRequest) {

        userPasswordService.createPassword(id, createRequest);
        return AysResponse.success();
    }

}
