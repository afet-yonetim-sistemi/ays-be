package com.ays.common.model;

import com.ays.common.util.validation.PhoneNumber;
import com.google.gson.Gson;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

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
    @Range(max = 5)
    private Long countryCode;

    /**
     * The line number of the phone number, a non-null long value between 1 and 10 (inclusive).
     */
    @NotNull
    @Range(max = 10)
    private Long lineNumber;

    /**
     * This method returns a JSON representation of the object for validation exception messages.
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
