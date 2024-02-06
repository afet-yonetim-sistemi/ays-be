package com.ays.common.model.dto.request;

import com.ays.common.util.AysJsonUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * A class representing a phone number in filtering requests, including its country code and line number.
 */
@Getter
@Setter
public class AysPhoneNumberFilterRequest {

    /**
     * The country code of the phone number
     */
    private String countryCode;

    /**
     * The line number of the phone number
     */
    private String lineNumber;

    /**
     * This method returns a JSON representation of the object for validation exception messages.
     */
    @Override
    public String toString() {
        return AysJsonUtil.toJson(this);
    }
}
