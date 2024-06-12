package org.ays.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.response.AysResponse;
import org.ays.user.model.User;
import org.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import org.ays.user.model.dto.response.UserSelfResponse;
import org.ays.user.model.mapper.UserToUserSelfResponseMapper;
import org.ays.user.service.UserSelfService;
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
