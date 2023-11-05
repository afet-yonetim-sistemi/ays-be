package com.ays.user.controller;

import com.ays.common.model.dto.response.AysResponse;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import com.ays.user.model.dto.response.UserSelfResponse;
import com.ays.user.model.mapper.UserToUserSelfResponseMapper;
import com.ays.user.service.UserSelfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Self Controller class for managing user's own operations.
 */
@RestController
@RequestMapping("/api/v1/user-self")
@RequiredArgsConstructor
class UserSelfController {

    private final UserSelfService userSelfService;

    private final UserToUserSelfResponseMapper userToUserSelfResponseMapper = UserToUserSelfResponseMapper.initialize();

    /**
     * Get user's self information.
     *
     * @return A response containing user's self information.
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<UserSelfResponse> getUserSelfInformation() {
        final User user = userSelfService.getUserSelfInformation();
        return AysResponse.successOf(userToUserSelfResponseMapper.map(user));
    }

    /**
     * Updates the support status of the current user.
     *
     * @param updateRequest the request object containing the updated support status
     * @return a response indicating the success of the operation
     */
    @PutMapping("/status/support")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<Void> updateUserSupportStatus(@RequestBody @Valid UserSupportStatusUpdateRequest updateRequest) {
        userSelfService.updateUserSupportStatus(updateRequest);
        return AysResponse.SUCCESS;
    }

}
