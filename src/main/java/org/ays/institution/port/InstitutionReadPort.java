package org.ays.institution.port;

import org.ays.institution.model.Institution;
import org.ays.institution.model.enums.InstitutionStatus;

import java.util.List;

/**
 * A read port interface for accessing {@link Institution} data.
 * Defines methods to retrieve {@link Institution} entities based on status and to check if an institution exists by ID and status.
 */
public interface InstitutionReadPort {

    /**
     * Retrieves a list of all {@link Institution} entities with the specified status, ordered by name in ascending order.
     *
     * @param status the status of the institutions to retrieve
     * @return a list of {@link Institution} entities with the specified status
     */
    List<Institution> findAllByStatusOrderByNameAsc(InstitutionStatus status);

}
