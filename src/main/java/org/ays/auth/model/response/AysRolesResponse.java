package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.AysRoleStatus;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for role responses.
 * <p>
 * The {@link AysRolesResponse} class encapsulates the information that is sent back
 * to the client as a response for role-related operations.
 * </p>
 */
@Getter
@Setter
public class AysRolesResponse {

    private String id;
    private String name;
    private AysRoleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
