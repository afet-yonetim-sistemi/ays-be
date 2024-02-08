package com.ays.common.model.dto.request;

import com.ays.common.util.AysJsonUtil;
import com.ays.common.util.validation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * A class representing a phone number in requests, including its country code and line number.
 */
@Getter
@Setter
@PhoneNumber
public class AysPhoneNumberRequest {

    /**
     * The country code of the phone number
     */
    @NotBlank
    private String countryCode;

    /**
     * The line number of the phone number
     */
    @NotBlank
    private String lineNumber;

    /**
     * This method returns a JSON representation of the object for validation exception messages.
     */
    @Override
    public String toString() {
        return AysJsonUtil.toJson(this);
    }

}
