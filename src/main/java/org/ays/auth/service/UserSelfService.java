package org.ays.auth.service;

import org.ays.auth.model.User;
import org.ays.auth.model.request.UserSupportStatusUpdateRequest;

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
