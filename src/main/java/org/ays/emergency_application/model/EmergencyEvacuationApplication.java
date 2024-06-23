package org.ays.emergency_application.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.BaseDomainModel;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.ays.institution.model.Institution;

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

    private Institution institution;

    /**
     * Marks the emergency evacuation application as pending.
     */
    public void pending() {
        this.referenceNumber = AysRandomUtil.generateNumber(10).toString();
        this.status = EmergencyEvacuationApplicationStatus.PENDING;
        this.isInPerson = this.applicantPhoneNumber == null;
    }

}
