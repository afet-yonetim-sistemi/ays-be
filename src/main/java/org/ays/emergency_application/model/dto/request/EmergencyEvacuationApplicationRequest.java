package org.ays.emergency_application.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.Name;
import org.hibernate.validator.constraints.Range;


/**
 * Represents a request to complete emergency evacuation request. The request includes fields for the required user
 * information, such as the user's phone number, as well as their first and last name.
 */
@Getter
@Setter
public class EmergencyEvacuationApplicationRequest {

    @Name
    @NotBlank
    private String firstName;

    @Name
    @NotBlank
    private String lastName;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

    @NotBlank
    @Size(min = 2, max = 100)
    private String sourceCity;

    @NotBlank
    @Size(min = 2, max = 100)
    private String sourceDistrict;

    @NotBlank
    private String address;

    @NotNull
    @Range(min = 1, max = 999)
    private Integer seatingCount;

    @NotBlank
    @Size(min = 2, max = 100)
    private String targetCity;

    @NotBlank
    @Size(min = 2, max = 100)
    private String targetDistrict;


    @Name
    private String applicantFirstName;

    @Name
    private String applicantLastName;

    @Valid
    private AysPhoneNumberRequest applicantPhoneNumber;

}
