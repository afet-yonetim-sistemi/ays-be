package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for role responses.
 * <p>
 * The {@link AysRolesSummaryResponse} class encapsulates the information that is sent back
 * to the client as a response for role-related operations.
 * </p>
 */
@Getter
@Setter
public class AysRolesSummaryResponse {

    private String id;
    private String name;

}
