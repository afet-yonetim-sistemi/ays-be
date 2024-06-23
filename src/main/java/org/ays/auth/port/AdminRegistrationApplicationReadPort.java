package org.ays.auth.port;

import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AdminRegistrationApplicationFilter;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;

import java.util.Optional;

/**
 * Read port interface for retrieving admin registration applications.
 * Implementations of this interface provide methods to find admin registration applications
 * based on pagination, filtering, and unique identifier criteria.
 */
public interface AdminRegistrationApplicationReadPort {

    /**
     * Retrieves a paginated list of admin registration applications based on provided pagination and filtering criteria.
     *
     * @param aysPageable The pagination configuration specifying page number, page size, and sort options.
     * @param filter      The filter criteria to apply when fetching applications. Can be {@code null} if no filtering is required.
     * @return A {@link AysPage} containing the paginated list of {@link AdminRegistrationApplication}.
     */
    AysPage<AdminRegistrationApplication> findAll(AysPageable aysPageable, AdminRegistrationApplicationFilter filter);

    /**
     * Finds an admin registration application by its unique identifier.
     *
     * @param id The unique identifier of the admin registration application to find.
     * @return An {@link Optional} containing the found {@link AdminRegistrationApplication}, or empty if not found.
     */
    Optional<AdminRegistrationApplication> findById(String id);

}
