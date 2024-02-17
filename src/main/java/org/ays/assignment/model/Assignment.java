package org.ays.assignment.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.Institution;
import org.ays.user.model.User;
import org.locationtech.jts.geom.Point;

/**
 * Assignment Domain Model to perform data transfer from service layer to controller
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Assignment extends BaseDomainModel {

    private String id;
    private String description;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private Point point;
    private AssignmentStatus status;

    private User user;
    private Institution institution;

}
