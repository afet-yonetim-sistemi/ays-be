package com.ays.common.model;

import com.ays.common.util.validation.PhoneNumber;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
    private Long countryCode;

    /**
     * The line number of the phone number, a non-null long value between 1 and 10 (inclusive).
     */
    @NotNull
    private Long lineNumber;

}
