package org.ays.auth.service;

import org.ays.auth.exception.AysEmailAddressNotValidException;
import org.ays.auth.exception.AysUserPasswordCannotChangedException;
import org.ays.auth.exception.AysUserPasswordDoesNotExistException;
import org.ays.auth.model.request.AysPasswordCreateRequest;
import org.ays.auth.model.request.AysPasswordForgotRequest;

/**
 * Service interface for handling user password operations.
 * Implementations of this interface should provide functionality for handling forgotten passwords.
 */
public interface AysUserPasswordService {

    /**
     * Handles the forgot password request by sending an email to the user
     * with instructions to create a new password.
     * <p>
     * This method checks if a user exists with the provided email address.
     * If the user exists and has no password set, a new temp password is generated.
     * If the user already has a password, the forgot password timestamp is updated.
     * In both cases, an email is sent to the user with instructions to create a new password.
     *
     * @param forgotPasswordRequest the request containing the user's email address.
     * @throws AysEmailAddressNotValidException if no user is found with the provided email address.
     */
    void forgotPassword(AysPasswordForgotRequest forgotPasswordRequest);

    /**
     * Checks the validity of changing the user's password.
     * <p>
     * This method verifies if the password change request is valid by checking the
     * existence and expiry of the password change request associated with the given password ID.
     * It throws an exception if the password cannot be changed due to invalid conditions.
     *
     * @param passwordId The ID of the password to check for change validity.
     * @throws AysUserPasswordDoesNotExistException  if no password is found for the given ID.
     * @throws AysUserPasswordCannotChangedException if the password cannot be changed due to invalid conditions.
     */
    void checkPasswordChangingValidity(String passwordId);

    /**
     * Creates a new password for the user.
     * <p>
     * This method updates the user's password with the new one provided in the request.
     * It validates the password change request before updating the password.
     *
     * @param passwordId    The unique identifier of the password to be created.
     * @param createRequest The request containing the new password details.
     */
    void createPassword(String passwordId, AysPasswordCreateRequest createRequest);

}
