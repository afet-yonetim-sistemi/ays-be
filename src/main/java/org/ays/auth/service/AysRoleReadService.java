package org.ays.auth.service;

import org.ays.auth.model.AysRole;
import org.ays.auth.model.request.AysRoleListRequest;
import org.ays.common.model.AysPage;

import java.util.Set;

/**
 * Service interface for reading and retrieving roles in a paginated format.
 * <p>
 * The {@link AysRoleReadService} provides a method for retrieving a paginated list of roles
 * based on the specified request parameters. Implementations of this interface should handle
 * the retrieval of roles, including any necessary filtering, sorting, and pagination logic
 * as defined in the {@link AysRoleListRequest}.
 * </p>
 *
 * @see AysRole
 * @see AysRoleListRequest
 * @see AysPage
 */
public interface AysRoleReadService {

    /**
     * Retrieves all roles.
     *
     * @return A set of {@link AysRole} objects representing all roles in the system.
     */
    Set<AysRole> findAll();

    /**
     * Retrieves a paginated list of roles based on the specified {@link AysRoleListRequest}.
     * <p>
     * This method handles the retrieval of roles from a data source according to the parameters
     * defined in the {@code listRequest}. It typically involves filtering, sorting, and paginating
     * the results to match the request criteria.
     * </p>
     *
     * @param listRequest the request containing parameters for filtering, sorting, and pagination.
     * @return a paginated list of roles matching the request criteria.
     */
    AysPage<AysRole> findAll(AysRoleListRequest listRequest);

}
