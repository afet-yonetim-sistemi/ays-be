package org.ays.user.service;

import org.ays.user.model.User;
import org.ays.user.model.dto.request.UserSupportStatusUpdateRequest;

/**
 * UserSelfService interface is responsible for managing a user's own operations
 */
public interface UserSelfService {

    User getUserSelfInformation();

    /**
     * Updates the support status of a user.
     *
     * @param updateRequest the request object containing the updated support status
     */
    void updateUserSupportStatus(UserSupportStatusUpdateRequest updateRequest);
}
