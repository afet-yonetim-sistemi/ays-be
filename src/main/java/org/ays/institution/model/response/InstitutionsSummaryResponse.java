package org.ays.institution.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the response for a single institution.
 * It includes information such as the institution's id and name.
 */
@Getter
@Setter
public class InstitutionsSummaryResponse {

    private String id;
    private String name;

}
