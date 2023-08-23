package com.ays.location.model.dto.request;

import com.ays.common.model.TestDataBuilder;

public class UserLocationSaveRequestBuilder extends TestDataBuilder<UserLocationSaveRequest> {

    public UserLocationSaveRequestBuilder() {
        super(UserLocationSaveRequest.class);
    }

    public UserLocationSaveRequestBuilder withValidFields() {
        return this
                .withLatitude(40.991739)
                .withLongitude(29.024168);
    }

    public UserLocationSaveRequestBuilder withLatitude(final Double latitude) {
        data.setLatitude(latitude);
        return this;
    }

    public UserLocationSaveRequestBuilder withLongitude(final Double longitude) {
        data.setLongitude(longitude);
        return this;
    }

}
