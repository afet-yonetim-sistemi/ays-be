package org.ays.emergency_application.port;

import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.filter.EmergencyEvacuationApplicationFilter;

import java.util.Optional;

/**
 * Read port interface for handling operations related to reading emergency evacuation applications.
 * <p>
 * This interface provides methods to support querying for emergency evacuation applications, including
 * retrieving paginated lists of applications and finding specific applications by their unique ID.
 * </p>
 */
public interface EmergencyEvacuationApplicationReadPort {

    /**
     * Retrieves a paginated list of emergency evacuation applications based on the provided pageable and filter criteria.
     * <p>
     * This method combines paging information encapsulated in {@link AysPageable} and filtering criteria specified
     * by {@link EmergencyEvacuationApplicationFilter} to perform a query and return the results in a paginated format.
     * </p>
     *
     * @param aysPageable the pageable object containing paging information
     * @param filter      the filter object containing criteria to filter the applications
     * @return a paginated list of {@link EmergencyEvacuationApplication} objects matching the criteria
     */
    AysPage<EmergencyEvacuationApplication> findAll(AysPageable aysPageable, EmergencyEvacuationApplicationFilter filter);

    /**
     * Finds an emergency evacuation application by its unique ID.
     * <p>
     * This method retrieves the application details based on the provided ID, if such an application exists.
     * </p>
     *
     * @param id the unique ID of the emergency evacuation application
     * @return an {@link Optional} containing the {@link EmergencyEvacuationApplication} if found, or empty if not found
     */
    Optional<EmergencyEvacuationApplication> findById(String id);

}
