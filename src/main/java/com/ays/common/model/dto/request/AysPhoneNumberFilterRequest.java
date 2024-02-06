package com.ays.common.model.dto.request;

import com.ays.common.util.AysJsonUtil;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1, max = 3, message = "COUNTRY CODE MUST BE BETWEEN 1 AND 3 CHARACTERS")
    private String countryCode;

    /**
     * The line number of the phone number
     */
    @Size(min = 7, max = 15, message = "LINE NUMBER MUST BE BETWEEN 7 AND 15 CHARACTERS")
    private String lineNumber;

    /**
     * This method returns a JSON representation of the object for validation exception messages.
     */
    @Override
    public String toString() {
        return AysJsonUtil.toJson(this);
    }
}
