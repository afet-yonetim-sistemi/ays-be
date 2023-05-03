package com.ays.common.model;

import com.ays.common.util.validation.PhoneNumber;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

/**
 * A class representing a phone number, including its country code and line number.
 * The phone number is validated using the custom PhoneNumber validation annotation.
 * This class is immutable and can be constructed using the Builder pattern.
 */
@Getter
@Builder
@PhoneNumber
public class AysPhoneNumber {

    /**
     * The country code of the phone number, a non-null integer value between 1 and 5 (inclusive).
     */
    @NotNull
    @Range(min = 1, max = 5)
    private Integer countryCode;

    /**
     * The line number of the phone number, a non-null integer value between 1 and 10 (inclusive).
     */
    @NotNull
    @Range(min = 1, max = 10)
    private Integer lineNumber;

}
