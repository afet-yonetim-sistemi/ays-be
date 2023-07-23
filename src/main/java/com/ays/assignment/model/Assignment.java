package com.ays.assignment.model;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.BaseDomainModel;
import com.ays.institution.model.Institution;
import com.ays.user.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * User Assignment Domain Model to perform data transfer from service layer to controller
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Assignment extends BaseDomainModel {

    private String id;
    private String description;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private Double latitude;
    private Double longitude;
    private AssignmentStatus status;

    private User user;
    private Institution institution;

}
