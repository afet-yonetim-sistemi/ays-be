package org.ays.auth.service;

import org.ays.auth.exception.AysUserAlreadyPassiveException;
import org.ays.auth.exception.AysUserNotActiveException;
import org.ays.auth.exception.AysUserNotExistByIdException;
import org.ays.auth.exception.AysUserNotPassiveException;
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

    /**
     * Activates a user by ID if the user is currently passive.
     *
     * @param id The unique identifier of the user to be activated.
     * @throws AysUserNotExistByIdException if a user with the given ID does not exist.
     * @throws AysUserNotPassiveException   if the user is not in a passive state.
     */
    void activate(String id);

    /**
     * Passivates (deactivates) a user by ID if the user is currently active.
     *
     * @param id The unique identifier of the user to be passivated.
     * @throws AysUserNotExistByIdException if a user with the given ID does not exist.
     * @throws AysUserAlreadyPassiveException if the user is already in a passive state.
     * @throws AysUserNotActiveException    if the user is not in an active state.
     */
    void passivate(String id);

    /**
     * Deletes a user by its unique identifier.
     *
     * @param id The unique identifier of the user to be deleted.
     */
    void delete(String id);

}
