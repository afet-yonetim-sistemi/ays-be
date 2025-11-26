package org.ays.institution.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for institution responses.
 * <p>
 * The {@link InstitutionsResponse} class encapsulates the information sent back
 * to the client as a response for institution-related operations.
 * </p>
 */
@Getter
@Setter
public class InstitutionsResponse {

    private String id;
    private String name;

}
