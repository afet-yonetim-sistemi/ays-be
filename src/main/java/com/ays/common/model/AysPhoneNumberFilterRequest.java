package com.ays.common.model;

import com.ays.common.util.validation.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PhoneNumber
public class AysPhoneNumberFilterRequest implements AysPhoneNumberAccessor {

    private String countryCode;
    private String lineNumber;
}
