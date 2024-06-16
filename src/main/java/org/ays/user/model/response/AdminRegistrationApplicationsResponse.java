package org.ays.user.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;

import java.time.LocalDateTime;

/**
 * Response class for admin registration applications.
 * <p>
 * This class contains information about admin registration applications,
 * including application status, institution details, user details, creator details,
 * and creation timestamp.
 * </p>
 */
@Getter
@Setter
public class AdminRegistrationApplicationsResponse {

    private String id;
    private String reason;
    private AdminRegistrationApplicationStatus status;
    private Institution institution;
    private User user;
    private String createdUser;
    private LocalDateTime createdAt;

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
    }

}
