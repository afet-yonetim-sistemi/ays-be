package com.ays.assignment.model.dto.response;

import com.ays.common.model.AysPhoneNumber;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssignmentSavedResponse {

    private String id;
    private String description;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private double latitude;
    private double longitude;
}
