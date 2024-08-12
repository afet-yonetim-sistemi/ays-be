package org.ays.emergency_application.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.AysPhoneNumber;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmergencyEvacuationApplicationResponse {

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
    private String createdUser;
    private LocalDateTime createdAt;
    private String updatedUser;
    private LocalDateTime updatedAt;

}
