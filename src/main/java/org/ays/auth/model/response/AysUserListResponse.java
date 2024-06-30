package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.AysPhoneNumber;

import java.util.UUID;

/**
 * Data Transfer Object for user responses.
 * <p>
 * The {@link AysUserListResponse} class encapsulates the information that is sent back
 * to the client as a response for user-related operations.
 * </p>
 */
@Getter
@Setter
public class AysUserListResponse {

    private UUID id;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private String city;
    private AysUserStatus status;

}
