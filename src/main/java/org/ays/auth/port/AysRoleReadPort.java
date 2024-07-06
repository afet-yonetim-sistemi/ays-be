package org.ays.auth.port;

import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleFilter;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;

import java.util.List;
import java.util.Optional;

/**
 * {@link AysRoleReadPort} is an interface for reading role data from the underlying data source.
 * <p>
 * It provides methods for retrieving roles based on various criteria such as pagination, filters,
 * institution ID, and role name. This interface is part of the application's hexagonal architecture,
 * facilitating the separation between domain logic and data access logic.
 * </p>
 */
public interface AysRoleReadPort {

    /**
     * Retrieves all roles.
     *
     * @return A list containing all roles.
     */
    List<AysRole> findAll();

    /**
     * Retrieves a paginated list of roles based on the provided pageable and filter.
     *
     * @param aysPageable The pagination configuration.
     * @param filter      The filter criteria for the roles.
     * @return A paginated list of roles.
     */
    AysPage<AysRole> findAll(AysPageable aysPageable, AysRoleFilter filter);

    /**
     * Retrieves a role by its unique identifier.
     *
     * @param id the unique identifier of the role.
     * @return a role.
     */
    Optional<AysRole> findById(String id);

    /**
     * Retrieves all active roles associated with a specific institution.
     *
     * @param institutionId The ID of the institution.
     * @return A list of active roles belonging to the institution.
     */
    List<AysRole> findAllActivesByInstitutionId(String institutionId);

    /**
     * Retrieves a role by its name.
     *
     * @param name The name of the role to retrieve.
     * @return An optional containing the role if found, otherwise empty.
     */
    Optional<AysRole> findByName(String name);

    /**
     * Checks if any users are assigned to a role identified by its ID.
     *
     * @param id The ID of the role to check.
     * @return true if users are assigned to the role, false otherwise.
     */
    boolean isRoleUsing(String id);

}
