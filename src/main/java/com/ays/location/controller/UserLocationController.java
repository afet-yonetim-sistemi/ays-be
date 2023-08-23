package com.ays.location.controller;

import com.ays.common.model.dto.response.AysResponse;
import com.ays.location.model.dto.request.UserLocationSaveRequest;
import com.ays.location.service.UserLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling user location-related API endpoints.
 * Provides endpoints for saving user location information.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class UserLocationController {

    private final UserLocationService userLocationService;

    /**
     * Saves the user's location based on the provided UserLocationSaveRequest.
     *
     * @param saveRequest The request containing the user's location information to be saved.
     * @return A response indicating the success of the operation.
     */
    @PostMapping("/user/location")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<Void> saveUserLocation(@RequestBody @Valid final UserLocationSaveRequest saveRequest) {
        userLocationService.saveUserLocation(saveRequest);
        return AysResponse.SUCCESS;
    }

}
