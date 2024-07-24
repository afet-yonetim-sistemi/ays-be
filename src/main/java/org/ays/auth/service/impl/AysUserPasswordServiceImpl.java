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
import org.ays.common.util.AysUUID;
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
     * Handles the forgot password process for a user.
     * <p>
     * This method is triggered when a user requests to reset their password. It checks if the user's email
     * address is valid and then sends a password reset email. If the user does not have an existing password,
     * a new temporary password is generated and saved.
     * </p>
     *
     * @param forgotPasswordRequest The request object containing the user's email address.
     * @throws AysEmailAddressNotValidException if the email address provided does not exist in the system.
     */
    @Override
    public void forgotPassword(final AysForgotPasswordRequest forgotPasswordRequest) {

        final String emailAddress = forgotPasswordRequest.getEmailAddress();
        final AysUser user = userReadPort.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new AysEmailAddressNotValidException(emailAddress));

        if (user.getPassword() != null) {
            userMailService.sendPasswordCreateEmail(user);
            return;
        }

        final AysUser.Password password = AysUser.Password.builder()
                .value(AysRandomUtil.generateUUID())
                .build();
        user.setPassword(password);
        AysUser savedUser = userSavePort.save(user);

        userMailService.sendPasswordCreateEmail(savedUser);
    }


    /**
     * Validates the provided password ID to ensure it is valid for password reset.
     * This method checks if the password ID exists, if the password value is in UUID format,
     * and if the password was updated within the last 2 hours.
     *
     * @param passwordId the ID of the password to be checked.
     * @throws AysUserPasswordDoesNotExistException  if the password ID does not exist.
     * @throws AysUserPasswordCannotChangedException if the password value is not in UUID format or if the password update time exceeds the allowed limit.
     */
    @Override
    public void checkPasswordChangingValidity(final String passwordId) {

        final AysUser.Password password = userReadPort.findByPasswordId(passwordId)
                .orElseThrow(() -> new AysUserPasswordDoesNotExistException(passwordId))
                .getPassword();

        boolean isUUID = AysUUID.isValid(password.getValue());
        if (!isUUID) {
            throw new AysUserPasswordCannotChangedException(passwordId);
        }

        LocalDateTime passwordChangedAt = Optional
                .ofNullable(password.getUpdatedAt())
                .orElse(password.getCreatedAt());
        boolean isExpired = LocalDateTime.now().minusHours(2).isBefore(passwordChangedAt);
        if (!isExpired) {
            throw new AysUserPasswordCannotChangedException(passwordId);
        }

    }

}
