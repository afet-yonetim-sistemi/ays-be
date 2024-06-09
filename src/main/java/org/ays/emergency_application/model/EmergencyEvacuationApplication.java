package org.ays.emergency_application.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.BaseDomainModel;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;

/**
 * Represents an emergency evacuation application.
 * Extends {@link BaseDomainModel} and includes details such as personal and contact information,
 * location details, and application status.
 */
@Getter
@Setter
@SuperBuilder
public class EmergencyEvacuationApplication extends BaseDomainModel {

    private String id;
    private String institutionId;
    private String referenceNumber;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private String sourceCity;
    private String sourceDistrict;
    private String address;
    private Integer seatingCount;
    private String targetCity;
    private String targetDistrict;
    private EmergencyEvacuationApplicationStatus status;
    private String applicantFirstName;
    private String applicantLastName;
    private AysPhoneNumber applicantPhoneNumber;
    private Boolean isInPerson;
    private Boolean hasObstaclePersonExist;
    private String notes;

}
