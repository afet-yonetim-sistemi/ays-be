package com.ays.user.model.dto.request;

import com.ays.common.util.validation.EnumValidation;
import com.ays.user.model.enums.UserSupportStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumSet;

/**
 * Request object for updating the support status of a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSupportStatusUpdateRequest {

    @NotNull
    private UserSupportStatus supportStatus;

    @AssertTrue(message = "SUPPORT STATUS IS NOT ACCEPTED!")
    private boolean isSupportStatusValid() {

        if (this.supportStatus == null) {
            return true;
        }

        EnumSet<UserSupportStatus> acceptedUserSupportStatuses = EnumSet.of(
                UserSupportStatus.IDLE,
                UserSupportStatus.READY,
                UserSupportStatus.BUSY,
                UserSupportStatus.MALFUNCTION,
                UserSupportStatus.ACCIDENT
        );

        return EnumValidation.anyOf(this.supportStatus, acceptedUserSupportStatuses);

    }

}
