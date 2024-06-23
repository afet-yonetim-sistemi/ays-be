package org.ays.institution.port;

import org.ays.institution.model.Institution;
import org.ays.institution.model.enums.InstitutionStatus;

import java.util.List;

/**
 * Port interface for reading {@link Institution} entities from a data store.
 * <p>
 * This interface defines the contract for retrieving institution entities, allowing various implementations
 * to handle data access based on the application’s requirements. It supports fetching institutions by their
 * status and checking the existence and activity of institutions by their ID.
 * </p>
 */
public interface InstitutionReadPort {

    /**
     * Retrieves a list of institutions by their status, ordered by their names in ascending order.
     * <p>
     * This method queries the data store for institutions with the specified status and orders them by name
     * in ascending order. It can be used to fetch institutions that are active, inactive, or any other defined status.
     * </p>
     *
     * @param status the status of the institutions to retrieve
     * @return a list of institutions with the specified status, ordered by name in ascending order
     */
    List<Institution> findAllByStatusOrderByNameAsc(InstitutionStatus status);

    /**
     * Checks if an institution with the given ID exists and has an active status.
     * <p>
     * This method queries the data store to determine if an institution with the specified ID exists and is active.
     * It is useful for validating the existence and current state of an institution.
     * </p>
     *
     * @param id the ID of the institution to check
     * @return true if an institution with the specified ID exists and is active, false otherwise
     */
    boolean existsByIdAndIsStatusActive(String id);

}
