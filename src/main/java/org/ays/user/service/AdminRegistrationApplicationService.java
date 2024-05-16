package org.ays.user.service;

import org.ays.common.model.AysPage;
import org.ays.user.model.AdminRegistrationApplication;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCreateRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationListRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationRejectRequest;

/**
 * Service interface for managing admin registration applications.
 * </p>
 * Provides methods for finding, creating, approving, and rejecting admin registration applications.
 */
public interface AdminRegistrationApplicationService {

    /**
     * Finds all admin registration applications based on the provided request criteria.
     *
     * @param listRequest The request criteria for listing applications.
     * @return A paginated list of admin registration applications.
     */
    AysPage<AdminRegistrationApplication> findAll(AdminRegistrationApplicationListRequest listRequest);

    /**
     * Finds an admin registration application by its unique ID.
     *
     * @param id The unique ID of the application.
     * @return The admin registration application with the specified ID.
     */
    AdminRegistrationApplication findById(String id);

    /**
     * Finds a summary of all admin registration applications by ID.
     *
     * @param id The unique ID of the application.
     * @return A summary of the admin registration application with the specified ID.
     */
    AdminRegistrationApplication findAllSummaryById(String id);

    /**
     * Creates a new admin registration application.
     *
     * @param request The request containing the details for creating a new application.
     * @return The newly created admin registration application.
     */
    AdminRegistrationApplication create(AdminRegistrationApplicationCreateRequest request);

    /**
     * Approves an admin registration application.
     *
     * @param id The unique ID of the application to be approved.
     */
    void approve(String id);

    /**
     * Rejects an admin registration application with a given request containing rejection details.
     *
     * @param id      The unique ID of the application to be rejected.
     * @param request The request containing the details for rejecting the application.
     */
    void reject(String id, AdminRegistrationApplicationRejectRequest request);

}
