package org.ays.landing.model.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.BaseDomainModel;
import org.ays.landing.model.entity.EmergencyEvacuationApplicationStatus;

/**
 * Domain model class representing an emergency evacuation application response.
 * <p>
 * This class contains details about the emergency evacuation application, including the application ID,
 * name, lastname, applicant name, lastname, and application status. It also holds references
 * to the associated institution - if handled by any.
 * </p>
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class EmergencyEvacuationApplicationResponse extends BaseDomainModel {

    private String id;
    private EmergencyEvacuationApplicationStatus status;
    private AysPhoneNumber userPhoneNumber;
    private AysPhoneNumber applicantPhoneNumber;

}
