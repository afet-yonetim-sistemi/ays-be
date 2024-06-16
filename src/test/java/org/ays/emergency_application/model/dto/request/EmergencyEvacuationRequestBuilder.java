package org.ays.emergency_application.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;

public class EmergencyEvacuationRequestBuilder extends TestDataBuilder<EmergencyEvacuationApplicationRequest> {

    public EmergencyEvacuationRequestBuilder() {
        super(EmergencyEvacuationApplicationRequest.class);
    }

    public EmergencyEvacuationRequestBuilder withValidFields() {
        return this
                .withSeatingCount(1)
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build())
                .withApplicantPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build())
                .withAddress("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
    }

    public EmergencyEvacuationRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withSourceCity(String sourceCity) {
        data.setSourceCity(sourceCity);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withSourceDistrict(String sourceDistrict) {
        data.setSourceDistrict(sourceDistrict);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withAddress(String address) {
        data.setAddress(address);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withSeatingCount(Integer seatingCount) {
        data.setSeatingCount(seatingCount);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withTargetCity(String targetCity) {
        data.setTargetCity(targetCity);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withTargetDistrict(String targetDistrict) {
        data.setTargetDistrict(targetDistrict);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withApplicantFirstName(String applicantFirstName) {
        data.setApplicantFirstName(applicantFirstName);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withApplicantLastName(String applicantLastName) {
        data.setApplicantLastName(applicantLastName);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withApplicantPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setApplicantPhoneNumber(phoneNumber);
        return this;
    }

    public EmergencyEvacuationRequestBuilder withoutApplicant() {
        data.setApplicantFirstName(null);
        data.setApplicantLastName(null);
        data.setApplicantPhoneNumber(null);
        return this;
    }

}
