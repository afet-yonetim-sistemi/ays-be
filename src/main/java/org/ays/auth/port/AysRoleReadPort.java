package org.ays.auth.port;

import org.ays.auth.model.AysRole;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for reading operations related to active roles by institution ID and by name.
 */
public interface AysRoleReadPort {

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

}
