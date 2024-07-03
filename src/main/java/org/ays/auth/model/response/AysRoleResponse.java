package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.AysPermissionCategory;
import org.ays.auth.model.enums.AysRoleStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AysRoleResponse {

    private String id;
    private String name;
    private AysRoleStatus status;
    private List<PermissionDetail> permissions;
    private String createdUser;
    private LocalDateTime createdAt;
    private String updatedUser;
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    public static class PermissionDetail {
        private String id;
        private String name;
        private AysPermissionCategory category;
    }

}
