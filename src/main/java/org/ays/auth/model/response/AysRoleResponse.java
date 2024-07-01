package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.enums.AysRoleStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AysRoleResponse {

    private String createdUser;
    private LocalDateTime createdAt;
    private String updatedUser;
    private LocalDateTime updatedAt;
    private String id;
    private String name;
    private AysRoleStatus status;
    private List<AysPermission> permissions;

}