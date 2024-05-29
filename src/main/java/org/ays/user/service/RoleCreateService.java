package org.ays.user.service;

import org.ays.user.model.dto.request.RoleCreateRequest;

/**
 * Service interface for creating roles.
 * This interface provides a method to create a role using the given request data.
 */
public interface RoleCreateService {

    /**
     * Creates a new role based on the provided {@link RoleCreateRequest}.
     *
     * @param createRequest the request object containing the details for the role to be created
     */
    void create(RoleCreateRequest createRequest);

}
