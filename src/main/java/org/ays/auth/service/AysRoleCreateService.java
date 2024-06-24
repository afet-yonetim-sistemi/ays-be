package org.ays.auth.service;

import org.ays.auth.model.request.AysRoleCreateRequest;

/**
 * Service interface for creating roles.
 * This interface provides a method to create a role using the given request data.
 */
public interface AysRoleCreateService {

    /**
     * Creates a new role based on the provided {@link AysRoleCreateRequest}.
     *
     * @param createRequest the request object containing the details for the role to be created
     */
    void create(AysRoleCreateRequest createRequest);

}
