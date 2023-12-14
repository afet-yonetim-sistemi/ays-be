package com.ays.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A Domain model representing a phone number, including its country code and line number.
 */
@Getter
@Setter
@Builder
public class AysPhoneNumber {

    /**
     * The country code of the phone number
     */
    private String countryCode;

    /**
     * The line number of the phone number
     */
    private String lineNumber;

}
