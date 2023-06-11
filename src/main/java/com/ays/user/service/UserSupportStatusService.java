package com.ays.user.service;

import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;

/**
 * User Support Status Service interface for managing the support status of a user.
 */
public interface UserSupportStatusService {

    /**
     * Updates the support status of a user.
     *
     * @param updateRequest the request object containing the updated support status
     */
    void updateUserSupportStatus(UserSupportStatusUpdateRequest updateRequest);
}
