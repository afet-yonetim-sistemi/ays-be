package com.ays.institution.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents the response for a single institution.
 * It includes information such as the institution's id and name.
 */
@Getter
@Setter
@SuperBuilder
public class InstitutionResponse {

    private String id;
    private String name;

}
