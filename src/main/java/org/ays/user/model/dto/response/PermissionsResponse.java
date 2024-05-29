package org.ays.user.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.user.model.enums.PermissionCategory;

@Getter
@Setter
public class PermissionsResponse {

    private String id;
    private String name;
    private PermissionCategory category;

}
