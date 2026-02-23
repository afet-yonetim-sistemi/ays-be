package org.ays.emergency_application.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.BaseDomainModel;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationPriority;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;
import org.ays.institution.model.Institution;

import java.util.Optional;

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
    private EmergencyEvacuationApplicationPriority priority;
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
     * Sets the emergency evacuation application to the {@code PENDING} state.
     * <p>
     * This method initializes the application for processing by:
     * <ul>
     *   <li>Generating a 10-digit reference number.</li>
     *   <li>Setting the default priority to {@link EmergencyEvacuationApplicationPriority#MEDIUM}.</li>
     *   <li>Updating the status to {@link EmergencyEvacuationApplicationStatus#PENDING}.</li>
     *   <li>Marking the application as in-person if {@code applicantPhoneNumber} is {@code null}.</li>
     *   <li>Resetting {@code hasObstaclePersonExist} to {@code false}.</li>
     * </ul>
     * </p>
     */
    public void pending() {
        this.referenceNumber = AysRandomUtil.generateNumber(10).toString();
        this.priority = EmergencyEvacuationApplicationPriority.MEDIUM;
        this.status = EmergencyEvacuationApplicationStatus.PENDING;
        this.isInPerson = this.applicantPhoneNumber == null;
        this.hasObstaclePersonExist = false;
    }

    /**
     * Updates the application with the provided values.
     * <p>
     * If the application does not yet have an associated institution, the institution reference is initialized
     * using the given {@code institutionId}. The {@code notes} field is updated only when the provided value is non-blank;
     * otherwise, the existing notes are preserved.
     * </p>
     *
     * @param institutionId          the institution ID to associate with the application if not already set
     * @param seatingCount           the seating count to set
     * @param priority               the priority to set
     * @param status                 the status to set
     * @param hasObstaclePersonExist whether an obstacle person exists flag to set
     * @param notes                  optional notes; applied only if non-blank
     */
    public void update(final String institutionId,
                       final Integer seatingCount,
                       final EmergencyEvacuationApplicationPriority priority,
                       final EmergencyEvacuationApplicationStatus status,
                       final Boolean hasObstaclePersonExist,
                       final String notes) {

        if (this.hasNotInstitution()) {
            this.institution = Institution.builder()
                    .id(institutionId)
                    .build();
        }

        this.seatingCount = seatingCount;
        this.priority = priority;
        this.status = status;
        this.hasObstaclePersonExist = hasObstaclePersonExist;
        this.notes = Optional.ofNullable(notes)
                .filter(StringUtils::isNotBlank)
                .orElse(this.notes);
    }

}
