package org.ays.common.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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
     * Converts the phone number object into a JSON-like string representation.
     * <p>
     * This method is primarily used for logging or validation exception messages.
     * It concatenates the country code and line number into a single string.
     * </p>
     *
     * @return A string representation of the phone number in the format: {@code countryCode + lineNumber}.
     */
    @Override
    public String toString() {
        return this.countryCode + this.lineNumber;
    }

    public boolean isBlank() {
        return StringUtils.isBlank(this.countryCode) && StringUtils.isBlank(this.lineNumber);
    }

}
