package org.ays.auth.service;

import org.ays.auth.model.AysUser;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.common.model.AysPage;

/**
 * Service interface for reading and retrieving users in a paginated format.
 * <p>
 * The {@link AysUserReadService} provides a method for retrieving a paginated list of users
 * based on the specified request parameters. Implementations of this interface should handle
 * the retrieval of users, including any necessary filtering, sorting, and pagination logic
 * as defined in the {@link AysUserListRequest}.
 * </p>
 *
 * @see AysUser
 * @see AysUserListRequest
 * @see AysPage
 */
public interface AysUserReadService {

    /**
     * Retrieves a paginated list of users based on the specified {@link AysUserListRequest}.
     * <p>
     * This method handles the retrieval of users from a data source according to the parameters
     * defined in the {@code listRequest}. It typically involves filtering, sorting, and paginating
     * the results to match the request criteria.
     * </p>
     *
     * @param listRequest the request containing parameters for filtering, sorting, and pagination.
     * @return a paginated list of users matching the request criteria.
     */
    AysPage<AysUser> findAll(AysUserListRequest listRequest);

    /**
     * Retrieves the details of a specific user by its ID.
     *
     * @param id The ID of the user.
     * @return The user with the specified ID, or null if not found.
     */
    AysUser findById(String id);

}
