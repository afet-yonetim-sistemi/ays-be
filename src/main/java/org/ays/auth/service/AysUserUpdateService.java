package org.ays.auth.service;

import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.util.exception.AysUserNotExistByIdException;
import org.ays.auth.util.exception.AysUserNotPassiveException;

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

    /**
     * Activates a user by ID if the user is currently passive.
     *
     * @param id The unique identifier of the user to be activated.
     * @throws AysUserNotExistByIdException if a user with the given ID does not exist.
     * @throws AysUserNotPassiveException if the user is not in a passive state.
     */
    void activate(String id);

}
