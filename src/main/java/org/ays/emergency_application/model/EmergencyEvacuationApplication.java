package org.ays.emergency_application.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.BaseDomainModel;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;
import org.ays.institution.model.Institution;

/**
 * Represents an emergency evacuation application.
 * Extends {@link BaseDomainModel} and includes details such as personal and contact information,
 * location details, and application status.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
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
     * Checks if the application does not have an associated institution.
     *
     * @return true if the institution is null, false otherwise.
     */
    public boolean hasNotInstitution() {
        return this.institution == null;
    }

    /**
     * Checks if the application is associated with the specified institution ID.
     *
     * @param institutionId the ID of the institution to check against.
     * @return true if the institution ID matches, false otherwise.
     */
    public boolean isInstitutionOwner(final String institutionId) {
        return this.institution.getId().equals(institutionId);
    }


    /**
     * Marks the emergency evacuation application as pending.
     * Generates a reference number and updates the status to pending.
     * Sets isInPerson based on whether the applicant's phone number is provided.
     */
    public void pending() {
        this.referenceNumber = AysRandomUtil.generateNumber(10).toString();
        this.status = EmergencyEvacuationApplicationStatus.PENDING;
        this.isInPerson = this.applicantPhoneNumber == null;
        this.hasObstaclePersonExist = false;
    }

    /**
     * Sets the institution ID for the application.
     *
     * @param institutionId the ID of the institution to associate with the application.
     */
    public void setInstitutionId(final String institutionId) {
        this.institution = Institution.builder()
                .id(institutionId)
                .build();
    }

}
