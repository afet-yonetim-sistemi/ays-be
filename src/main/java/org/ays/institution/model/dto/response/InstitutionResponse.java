package org.ays.institution.model.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.response.BaseResponse;

/**
 * This class represents the response for a single institution.
 * It includes information such as the institution's id and name.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class InstitutionResponse extends BaseResponse {

    private String id;
    private String name;

}
