package com.ays.location.model;

import com.ays.common.model.BaseDomainModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

/**
 * Location Domain Model to perform data transfer from service layer to controller
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Location extends BaseDomainModel {

    private Long id;
    private Point point;
}
