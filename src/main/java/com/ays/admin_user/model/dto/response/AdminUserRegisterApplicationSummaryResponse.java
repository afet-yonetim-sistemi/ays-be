package com.ays.admin_user.model.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO (Data Transfer Object) representing a list of admin user register applications
 */
@Getter
@Setter
public class AdminUserRegisterApplicationSummaryResponse {

    private String id;
    private Institution institution;

    /**
     * A DTO (Data Transfer Object) representing an institution in an admin user register application
     */
    @Getter
    @Setter
    public static class Institution {
        private String id;
        private String name;
    }
}
