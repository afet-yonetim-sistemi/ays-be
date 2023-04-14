package com.ays.common.model;

import com.ays.common.util.validation.PhoneNumber;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@Builder
@PhoneNumber
public class AysPhoneNumber {

    @NotNull
    @Range(min = 1, max = 5)
    private Integer countryCode;

    @NotNull
    @Range(min = 1, max = 10)
    private Integer lineNumber;

}
