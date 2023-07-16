package com.ays.user.model.dto.request;

import com.ays.common.util.validation.EnumValidation;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

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
@Data
@Builder
public class UserUpdateRequest {

    @NotNull
    private UserRole role;

    @NotNull
    private UserStatus status;

    @AssertTrue(message = "IS NOT ACCEPTED")
    @SuppressWarnings("unused")
    private boolean isStatusAccepted() {

        if (ObjectUtils.isEmpty(this.status)) {
            return true;
        }

        EnumSet<UserStatus> acceptedUserStatuses = EnumSet.of(UserStatus.ACTIVE, UserStatus.PASSIVE);
        return EnumValidation.anyOf(this.status, acceptedUserStatuses);
    }

    @AssertTrue(message = "IS NOT ACCEPTED")
    @SuppressWarnings("unused")
    private boolean isRoleAccepted() {

        if (ObjectUtils.isEmpty(this.role)) {
            return true;
        }

        EnumSet<UserRole> acceptedUserRoles = EnumSet.of(UserRole.VOLUNTEER);
        return EnumValidation.anyOf(this.role, acceptedUserRoles);
    }

}
