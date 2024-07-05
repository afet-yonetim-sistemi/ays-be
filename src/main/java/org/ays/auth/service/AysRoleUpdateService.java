package org.ays.auth.service;

import org.ays.auth.model.request.AysRoleUpdateRequest;

/**
 * Service interface for updating roles.
 * Implementations of this interface should provide functionality to update an existing role
 * based on the provided update request.
 */
public interface AysRoleUpdateService {

    /**
     * Service interface for updating roles.
     * Implementations of this interface should provide functionality to update an existing role
     * based on the provided update request.
     */
    void update(String id, AysRoleUpdateRequest updateRequest);

}
