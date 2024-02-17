package org.ays.common.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.AysJsonUtil;
import org.ays.common.util.validation.PhoneNumber;

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
