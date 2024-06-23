package org.ays.auth.port;

import org.ays.auth.model.AysRole;

import java.util.Optional;
import java.util.Set;

/**
 * Port interface for reading operations related to active roles by institution ID and by name.
 */
public interface AysRoleReadPort {

    /**
     * Retrieves all active roles associated with a specific institution.
     *
     * @param institutionId The ID of the institution.
     * @return A set of active roles belonging to the institution.
     */
    Set<AysRole> findAllActivesByInstitutionId(String institutionId);

    /**
     * Retrieves a role by its name.
     *
     * @param name The name of the role to retrieve.
     * @return An optional containing the role if found, otherwise empty.
     */
    Optional<AysRole> findByName(String name);

}
