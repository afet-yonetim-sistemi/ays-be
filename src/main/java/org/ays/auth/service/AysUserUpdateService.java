package org.ays.auth.service;

import org.ays.auth.model.request.AysUserUpdateRequest;

/**
 * Service interface for updating users.
 * Implementations of this interface should provide the functionality to update an existing user
 * based on the provided update request.
 */
public interface AysUserUpdateService {

    /**
     * Updates the user with the specified ID based on the provided update request.
     */
    void update(String id, AysUserUpdateRequest updateRequest);

}
