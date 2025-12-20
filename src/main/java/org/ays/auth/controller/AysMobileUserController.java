package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.request.AysMobileUserRegisterRequest;
import org.ays.auth.service.AysMobileUserRegisterService;
import org.ays.common.model.response.AysResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for mobile user operations.
 * <p>
 * This controller provides public endpoints for mobile app users,
 * including self-registration functionality.
 * </p>
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mobile/v1")
public class AysMobileUserController {

    private final AysMobileUserRegisterService mobileUserRegisterService;

    /**
     * Registers a new mobile user.
     * <p>
     * This is a public endpoint that allows users to self-register
     * through the mobile application. The user will be created with
     * NOT_VERIFIED status until email verification is completed.
     * </p>
     *
     * @param registerRequest The registration request containing user details.
     * @return A success response indicating registration was successful.
     */
    @PostMapping("/user/register")
    public AysResponse<Void> register(@RequestBody @Valid AysMobileUserRegisterRequest registerRequest) {
        mobileUserRegisterService.register(registerRequest);
        return AysResponse.success();
    }

}
