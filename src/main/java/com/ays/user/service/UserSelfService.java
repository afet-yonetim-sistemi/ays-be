package com.ays.user.service;

import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;

/**
 * UserSelfService interface is responsible for managing a user's own operations
 */
public interface UserSelfService {

    /**
     * Updates the support status of a user.
     *
     * @param updateRequest the request object containing the updated support status
     */
    void updateUserSupportStatus(UserSupportStatusUpdateRequest updateRequest);
}
