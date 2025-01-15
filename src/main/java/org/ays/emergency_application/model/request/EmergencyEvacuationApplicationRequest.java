package org.ays.emergency_application.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.util.validation.Name;
import org.hibernate.validator.constraints.Range;

/**
 * Represents a request to complete emergency evacuation request. The request
 * includes fields for the required user
 * information, such as the user's phone number, as well as their first and last
 * name.
 */
@Getter
@Setter
public class EmergencyEvacuationApplicationRequest {

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @Valid
    @NotNull
    private AysPhoneNumberRequest phoneNumber;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String sourceCity;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String sourceDistrict;

    @NotBlank
    @Size(min = 20, max = 250)
    private String address;

    @NotNull
    @Range(min = 1, max = 999)
    private Integer seatingCount;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String targetCity;

    @Name
    @NotBlank
    @Size(min = 2, max = 100)
    private String targetDistrict;

    @Name
    @Size(min = 2, max = 100)
    private String applicantFirstName;

    @Name
    @Size(min = 2, max = 100)
    private String applicantLastName;

    @Valid
    private AysPhoneNumberRequest applicantPhoneNumber;

    @NotNull
    private Boolean hasObstaclePersonExist;

    @JsonIgnore
    @AssertTrue(message = "all applicant fields must be filled")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isAllApplicantFieldsFilled() {

        if (StringUtils.isEmpty(this.applicantFirstName) && StringUtils.isEmpty(this.applicantLastName)
                && this.applicantPhoneNumber == null) {
            return true;
        }

        return !StringUtils.isBlank(this.applicantFirstName) && !StringUtils.isBlank(this.applicantLastName)
                &&
                this.applicantPhoneNumber != null
                &&
                !(StringUtils.isBlank(this.applicantPhoneNumber.getCountryCode())
                        && StringUtils.isBlank(this.applicantPhoneNumber.getLineNumber()));
    }

    @JsonIgnore
    @AssertTrue(message = "phone numbers must not be same one")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isPhoneNumberMustNotBeSameOne() {

        if (this.applicantPhoneNumber == null) {
            return true;
        }

        if (StringUtils.isEmpty(this.applicantPhoneNumber.getLineNumber())
                || StringUtils.isEmpty(this.phoneNumber.getLineNumber())) {
            return true;
        }

        return !this.applicantPhoneNumber.getLineNumber().equals(this.phoneNumber.getLineNumber());
    }

    @JsonIgnore
    @AssertTrue(message = "source city/district and target city/district must be different")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isSourceCityAndDistrictDifferentFromTargetCityAndDistrict() {

        if (this.sourceCity == null || this.sourceDistrict == null || this.targetCity == null
                || this.targetDistrict == null) {
            return true;
        }

        if (!this.sourceCity.equalsIgnoreCase(this.targetCity)) {
            return true;
        }

        return !this.sourceDistrict.equalsIgnoreCase(this.targetDistrict);
    }

}
