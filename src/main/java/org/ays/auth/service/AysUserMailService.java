package org.ays.auth.service;

import org.ays.auth.model.AysUser;

/**
 * Service interface for managing user email operations.
 * <p>
 * This interface defines the contract for sending various types of emails to users,
 * particularly for password creation and reset operations.
 * Implementations of this interface are responsible for constructing and sending emails
 * to the specified users based on the provided data.
 * </p>
 */
public interface AysUserMailService {

    /**
     * Sends an email to a user with instructions to create a new password.
     * <p>
     * This method is intended to be used when a user needs to create or reset their password.
     * It constructs an email with the necessary details and sends it to the user's registered email address.
     * </p>
     *
     * @param user The user to whom the password creation email will be sent.
     */
    void sendPasswordCreateEmail(AysUser user);

}
