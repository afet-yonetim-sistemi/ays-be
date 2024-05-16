package org.ays.admin_user.model.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Summary response class for an admin user registration application.
 * <p>
 * This class provides a summary of the registration application,
 * including the application ID and institution details.
 * </p>
 */
@Getter
@Setter
public class AdminRegisterApplicationSummaryResponse {

    private String id;
    private Institution institution;

    /**
     * Nested static class representing the institution details.
     */
    @Getter
    @Setter
    public static class Institution {
        private String name;
    }

}
