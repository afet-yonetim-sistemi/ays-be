package org.ays.location.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.BaseDomainModel;
import org.locationtech.jts.geom.Point;

/**
 * Location Domain Model to perform data transfer from service layer to controller
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserLocation extends BaseDomainModel {

    private Long id;
    private Point point;
}
