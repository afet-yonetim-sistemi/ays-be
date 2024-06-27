package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.AysPhoneNumber;

import java.util.UUID;

// todo javadoc

@Getter
@Setter
public class AysUserListResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private AysPhoneNumber phoneNumber;
    private String city;
    private AysUserStatus status;

}
