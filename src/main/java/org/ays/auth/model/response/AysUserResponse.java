package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.AysPhoneNumber;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AysUserResponse {

    private String id;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private AysPhoneNumber phoneNumber;
    private String city;
    private AysUserStatus status;
    private List<Role> roles;
    private String createdUser;
    private LocalDateTime createdAt;
    private String updatedUser;
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    public static class Role {
        private String id;
        private String name;
    }
}
