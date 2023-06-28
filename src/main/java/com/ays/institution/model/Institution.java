package com.ays.institution.model;

import com.ays.common.model.BaseDomainModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Institution Domain Model to perform data transfer from service layer to controller
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Institution extends BaseDomainModel {

    private String id;

    private String name;

}
