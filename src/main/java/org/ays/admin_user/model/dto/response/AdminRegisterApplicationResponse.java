package org.ays.admin_user.model.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.admin_user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.dto.response.BaseResponse;

/**
 * Response class for admin user registration application.
 * <p>
 * This class extends {@link BaseResponse} and contains information about the admin user registration application,
 * including the application status, institution details, and user details.
 * </p>
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AdminRegisterApplicationResponse extends BaseResponse {

    private String id;
    private String reason;
    private String rejectReason;
    private AdminRegistrationApplicationStatus status;
    private Institution institution;
    private User user;

    /**
     * Nested static class representing the institution details.
     */
    @Getter
    @Setter
    public static class Institution {
        private String id;
        private String name;
    }

    /**
     * Nested static class representing the user details.
     */
    @Getter
    @Setter
    public static class User {
        private String id;
        private String firstName;
        private String lastName;
        private String city;
        private String emailAddress;
        private AysPhoneNumber phoneNumber;
    }

}
