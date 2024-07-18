package org.ays.auth.service;

import org.ays.auth.model.request.AysForgotPasswordRequest;

/**
 * Service interface for handling user password operations.
 * Implementations of this interface should provide functionality for handling forgotten passwords.
 */
public interface AysUserPasswordService {

    /**
     * Handles the forgot password request by sending an email to the user
     * with instructions to create a new password.
     *
     * @param forgotPasswordRequest the request containing the user's email address.
     */
    void forgotPassword(AysForgotPasswordRequest forgotPasswordRequest);

}
