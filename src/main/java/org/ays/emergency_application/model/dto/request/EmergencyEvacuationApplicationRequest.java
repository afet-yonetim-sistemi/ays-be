package org.ays.emergency_application.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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

    @Name
    @NotBlank
    private String sourceCity;

    @Name
    @NotBlank
    private String sourceDistrict;

    @NotBlank
    @Size(min = 20, max = 250)
    private String address;

    @NotNull
    @Range(min = 1, max = 999)
    private Integer seatingCount;

    @Name
    @NotBlank
    private String targetCity;

    @Name
    @NotBlank
    private String targetDistrict;


    @Name
    private String applicantFirstName;

    @Name
    private String applicantLastName;

    @Valid
    private AysPhoneNumberRequest applicantPhoneNumber;


    @JsonIgnore
    @AssertTrue(message = "all applicant fields must be filled")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isAllApplicantFieldsFilled() {

        if (StringUtils.isEmpty(this.applicantFirstName) && StringUtils.isEmpty(this.applicantLastName) && this.applicantPhoneNumber == null) {
            return true;
        }

        return !StringUtils.isEmpty(this.applicantFirstName) && !StringUtils.isEmpty(this.applicantLastName)
                &&
                this.applicantPhoneNumber != null && !this.applicantPhoneNumber.isEmpty();
    }

    @JsonIgnore
    @AssertTrue(message = "phone numbers must not be same one")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isPhoneNumberMustNotBeSameOne() {

        if (this.applicantPhoneNumber == null) {
            return true;
        }

        return !this.applicantPhoneNumber.getLineNumber().equals(this.phoneNumber.getLineNumber());
    }

}
