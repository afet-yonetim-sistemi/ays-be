package org.ays.auth.service.impl;

import org.ays.auth.exception.AysUserDoesNotAccessPageException;
import org.ays.auth.exception.AysUserNotActiveException;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysSourcePage;
import org.ays.auth.service.AysUserValidateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AysUserValidateServiceImpl implements AysUserValidateService {

    /**
     * Validates the status of the user.
     * Throws {@link AysUserNotActiveException} if the user is not active.
     *
     * @param user The {@link AysUser} object whose status needs to be validated.
     * @throws AysUserNotActiveException If the user is not active.
     */
    @Override
    public void validateUserStatus(final AysUser user) {

        if (!user.isActive()) {
            throw new AysUserNotActiveException(user.getId());
        }
    }

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
    @Override
    public void validateUserSourcePagePermission(final AysUser user,
                                                 final AysSourcePage sourcePage) {

        boolean hasUserPermission = user.getRoles().stream()
                .map(AysRole::getPermissions)
                .flatMap(List::stream)
                .anyMatch(permission -> permission.getName().equals(sourcePage.getPermission()));

        if (!hasUserPermission) {
            throw new AysUserDoesNotAccessPageException(user.getId(), sourcePage);
        }
    }
}
