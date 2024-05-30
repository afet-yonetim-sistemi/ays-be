package org.ays.emergency_application.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.BaseDomainModel;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;

// TODO AYS-222 : Add Javadoc
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class EmergencyEvacuationApplication extends BaseDomainModel {
    private String id;
    private String institutionId;
    private String referenceNumber;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String lineNumber;
    private String sourceCity;
    private String sourceDistrict;
    private String address;
    private Integer seatingCount;
    private String targetCity;
    private String targetDistrict;
    private EmergencyEvacuationApplicationStatus status;
    private String applicantFirstName;
    private String applicantLastName;
    private String applicantCountryCode;
    private String applicantLineNumber;
    private Boolean isInPerson;
    private Boolean hasObstaclePersonExist;
    private String notes;
}
