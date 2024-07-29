package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.request.AysForgotPasswordRequest;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysUserMailService;
import org.ays.auth.service.AysUserPasswordService;
import org.ays.auth.util.exception.AysEmailAddressNotValidException;
import org.ays.auth.util.exception.AysUserPasswordCannotChangedException;
import org.ays.auth.util.exception.AysUserPasswordDoesNotExistException;
import org.ays.common.util.AysRandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service implementation for managing user password operations.
 * <p>
 * This class provides the business logic for handling user password-related operations,
 * such as sending password reset emails and updating user passwords in the system.
 * It interacts with various ports for reading and saving user data and sending emails.
 * </p>
 */
@Service
@Transactional
@RequiredArgsConstructor
class AysUserPasswordServiceImpl implements AysUserPasswordService {

    private final AysUserReadPort userReadPort;
    private final AysUserSavePort userSavePort;
    private final AysUserMailService userMailService;


    /**
     * Handles the forgot password request by sending an email to the user
     * with instructions to create a new password.
     * <p>
     * This method checks if a user exists with the provided email address.
     * If the user exists and has no password set, a new temp password is generated.
     * If the user already has a password, the forgot password timestamp is updated.
     * In both cases, an email is sent to the user with instructions to create a new password.
     *
     * @param forgotPasswordRequest The request containing the user's email address.
     * @throws AysEmailAddressNotValidException if no user is found with the provided email address.
     */
    @Override
    public void forgotPassword(final AysForgotPasswordRequest forgotPasswordRequest) {

        final String emailAddress = forgotPasswordRequest.getEmailAddress();
        final AysUser user = userReadPort.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new AysEmailAddressNotValidException(emailAddress));

        if (user.getPassword() == null) {
            final AysUser.Password password = AysUser.Password.builder()
                    .value(AysRandomUtil.generateUUID())
                    .forgotAt(LocalDateTime.now())
                    .build();
            user.setPassword(password);
        } else {
            user.getPassword().setForgotAt(LocalDateTime.now());
        }

        final AysUser savedUser = userSavePort.save(user);
        userMailService.sendPasswordCreateEmail(savedUser);
    }


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
    @Override
    public void checkPasswordChangingValidity(final String passwordId) {

        final AysUser.Password password = userReadPort.findByPasswordId(passwordId)
                .orElseThrow(() -> new AysUserPasswordDoesNotExistException(passwordId))
                .getPassword();

        this.checkChangingValidity(password);
    }


    /**
     * Checks the validity of changing the password.
     * <p>
     * This method verifies if the password change request is valid by checking if the password change request
     * was initiated within the allowable time frame. It throws an exception if the password cannot be changed.
     *
     * @param password The AysUser.Password object representing the user's password.
     * @throws AysUserPasswordCannotChangedException if the password cannot be changed due to invalid conditions.
     */
    private void checkChangingValidity(final AysUser.Password password) {

        Optional<LocalDateTime> forgotAt = Optional
                .ofNullable(password.getForgotAt());

        if (forgotAt.isEmpty()) {
            throw new AysUserPasswordCannotChangedException(password.getId());
        }

        boolean isExpired = LocalDateTime.now().minusHours(2).isBefore(forgotAt.get());
        if (!isExpired) {
            throw new AysUserPasswordCannotChangedException(password.getId());
        }

    }

}
