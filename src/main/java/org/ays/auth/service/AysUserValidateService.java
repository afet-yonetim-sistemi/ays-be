package org.ays.auth.service;

import org.ays.auth.exception.AysUserDoesNotAccessPageException;
import org.ays.auth.exception.AysUserNotActiveException;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysSourcePage;

public interface AysUserValidateService {

    /**
     * Validates the status of the user.
     * Throws {@link AysUserNotActiveException} if the user is not active.
     *
     * @param user The {@link AysUser} object whose status needs to be validated.
     * @throws AysUserNotActiveException If the user is not active.
     */
    void validateUserStatus(final AysUser user);

    /**
     * Validates whether the user has permission to access the specified source page.
     *
     * <p>
     * This method checks if the user's roles contain any permissions associated with the specified source page.
     * If the user has permission, it returns true; otherwise, it throws a {@link AysUserDoesNotAccessPageException}.
     *
     * @param user       The user for which permissions are to be checked.
     * @param sourcePage The source page for which permission is to be validated.
     * @throws AysUserDoesNotAccessPageException If the user does not have permission to access the specified source page.
     */
    void validateUserSourcePagePermission(final AysUser user, final AysSourcePage sourcePage);
}
