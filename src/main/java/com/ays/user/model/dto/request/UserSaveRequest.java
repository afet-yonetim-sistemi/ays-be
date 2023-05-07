package com.ays.user.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSaveRequest {

    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;

}
