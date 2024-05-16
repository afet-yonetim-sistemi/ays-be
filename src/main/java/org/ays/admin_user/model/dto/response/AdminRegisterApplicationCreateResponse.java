package org.ays.admin_user.model.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Response class for creating an admin user registration application.
 * <p>
 * This class contains the ID of the newly created registration application.
 * </p>
 */
@Getter
@Setter
public class AdminRegisterApplicationCreateResponse {

    private String id;

}
