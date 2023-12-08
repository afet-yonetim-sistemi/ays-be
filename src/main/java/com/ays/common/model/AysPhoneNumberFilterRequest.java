package com.ays.common.model;

import com.ays.common.util.validation.PhoneNumber;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

/**
 * A class representing a phone number in filtering requests, including its country code and line number.
 */
@Getter
@Setter
@PhoneNumber
public class AysPhoneNumberFilterRequest implements AysPhoneNumberAccessor {

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
        return new Gson().toJson(this);
    }
}
