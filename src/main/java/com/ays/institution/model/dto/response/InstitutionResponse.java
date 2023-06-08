package com.ays.institution.model.dto.response;

import com.ays.common.model.dto.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * This class represents the response for a single institution.
 * It includes information such as the institution's id and name.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class InstitutionResponse extends BaseResponse {

    private String id;

    private String name;

}
