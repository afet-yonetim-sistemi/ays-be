package com.ays.common.model;

import com.ays.common.util.validation.PhoneNumber;
import com.google.gson.Gson;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * A class representing a phone number, including its country code and line number.
 * The phone number is validated using the custom PhoneNumber validation annotation.
 * This class is immutable and can be constructed using the Builder pattern.
 */
@Getter
@Setter
@Builder
@PhoneNumber
public class AysPhoneNumber {

    /**
     * The country code of the phone number, a non-null long value between 1 and 5 (inclusive).
     */
    @NotNull
    @Pattern(regexp = "^\\d{1,5}$")
    @Length(max = 5)
    private String countryCode;

    /**
     * The line number of the phone number, a non-null long value between 1 and 10 (inclusive).
     */
    @NotNull
    @Pattern(regexp = "^\\d{1,10}$")
    @Length(max = 10)
    private String lineNumber;

    /**
     * This method returns a JSON representation of the object for validation exception messages.
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
