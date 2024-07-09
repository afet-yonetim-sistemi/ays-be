package org.ays.auth.service;

import org.ays.auth.model.request.AysUserUpdateRequest;

/**
 * Service interface for updating users.
 * Implementations of this interface should provide functionality to update an existing user
 * based on the provided update request.
 */
public interface AysUserUpdateService {

    void update(String id, AysUserUpdateRequest updateRequest);
}
