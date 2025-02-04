package org.ays.common.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
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
    @Pattern(regexp = "90", message = "Currently, only 90 country code is allowed.")
    private String countryCode;

    /**
     * The line number of the phone number
     */
    @NotBlank
    private String lineNumber;

    /**
     * Returns a string representation of the phone number object.
     * <p>
     * This method concatenates the country code and line number into a single string
     * without JSON formatting. It is primarily intended for internal use, such as
     * validation error messages or logging.
     * </p>
     *
     * @return A concatenated string of the country code and line number in the format: {@code countryCode + lineNumber}.
     */
    @Override
    public String toString() {
        return this.countryCode + this.lineNumber;
    }

}
