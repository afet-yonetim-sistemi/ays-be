package org.ays.location.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.location.model.dto.request.UserLocationSaveRequest;
import org.ays.location.service.UserLocationService;
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
