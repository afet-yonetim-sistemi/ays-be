package org.ays.auth.port;

import org.ays.auth.model.AdminRegistrationApplication;

/**
 * Save port interface for persisting admin registration applications.
 * Implementations of this interface provide a method to save or update admin registration applications.
 */
public interface AdminRegistrationApplicationSavePort {

    /**
     * Saves or updates the given admin registration application.
     *
     * @param registrationApplication The admin registration application to be saved or updated.
     * @return The saved or updated {@link AdminRegistrationApplication}.
     */
    AdminRegistrationApplication save(AdminRegistrationApplication registrationApplication);

}
