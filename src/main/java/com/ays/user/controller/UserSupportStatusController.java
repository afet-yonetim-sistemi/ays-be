package com.ays.user.controller;

import com.ays.common.model.dto.response.AysResponse;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import com.ays.user.service.UserSupportStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing the support status of a user.
 */
@RestController
@RequestMapping("/api/v1/user/self/status/support")
@RequiredArgsConstructor
public class UserSupportStatusController {

    private final UserSupportStatusService supportStatusService;

    /**
     * Updates the support status of the current user.
     *
     * @param updateRequest the request object containing the updated support status
     * @return a response indicating the success of the operation
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<Void> updateUserSupportStatus(@RequestBody @Valid UserSupportStatusUpdateRequest updateRequest) {
        supportStatusService.updateUserSupportStatus(updateRequest);
        return AysResponse.SUCCESS;
    }

}
