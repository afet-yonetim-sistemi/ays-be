package org.ays.emergency_application.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.AysPhoneNumber;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationPriority;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmergencyEvacuationApplicationsResponse {

    private String id;
    private String referenceNumber;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private String applicantFirstName;
    private String applicantLastName;
    private AysPhoneNumber applicantPhoneNumber;
    private Integer seatingCount;
    private EmergencyEvacuationApplicationPriority priority;
    private EmergencyEvacuationApplicationStatus status;
    private Boolean isInPerson;
    private LocalDateTime createdAt;

}
