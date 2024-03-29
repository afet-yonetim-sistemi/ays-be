package org.ays.user.model.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.ays.common.util.validation.EnumValidation;
import org.ays.user.model.enums.UserRole;
import org.ays.user.model.enums.UserStatus;

import java.util.EnumSet;

/**
 * Represents a request object for updating user with its variables.
 * <p>
 * This class provides getters and setters for the user role, user status fields.
 * It also includes a builder pattern implementation for constructing instances of this class with optional parameters.
 * <p>
 * The purpose of this class is to encapsulate the request data related to updating a user, allowing for easy
 * transfer of the data between different layers of the application.
 */
@Getter
@Setter
public class UserUpdateRequest {

    @NotNull
    private UserRole role;

    @NotNull
    private UserStatus status;

    @AssertTrue(message = "IS NOT ACCEPTED")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isStatusAccepted() {

        if (ObjectUtils.isEmpty(this.status)) {
            return true;
        }

        EnumSet<UserStatus> acceptedUserStatuses = EnumSet.of(UserStatus.ACTIVE, UserStatus.PASSIVE);
        return EnumValidation.anyOf(this.status, acceptedUserStatuses);
    }

    @AssertTrue(message = "IS NOT ACCEPTED")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isRoleAccepted() {

        if (ObjectUtils.isEmpty(this.role)) {
            return true;
        }

        EnumSet<UserRole> acceptedUserRoles = EnumSet.of(UserRole.VOLUNTEER);
        return EnumValidation.anyOf(this.role, acceptedUserRoles);
    }

}
