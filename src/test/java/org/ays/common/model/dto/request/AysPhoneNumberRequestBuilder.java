package org.ays.common.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.util.AysRandomUtil;

public class AysPhoneNumberRequestBuilder extends TestDataBuilder<AysPhoneNumberRequest> {

    public AysPhoneNumberRequestBuilder() {
        super(AysPhoneNumberRequest.class);
    }

    public AysPhoneNumberRequestBuilder withValidFields() {
        return new AysPhoneNumberRequestBuilder()
                .withCountryCode("90")
                .withLineNumber("535" + AysRandomUtil.generateNumber(7));
    }

    public AysPhoneNumberRequestBuilder withCountryCode(String countryCode) {
        data.setCountryCode(countryCode);
        return this;
    }

    public AysPhoneNumberRequestBuilder withLineNumber(String lineNumber) {
        data.setLineNumber(lineNumber);
        return this;
    }

}
