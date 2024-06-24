package org.ays.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.enums.PermissionCategory;

@Getter
@Setter
public class AysPermissionsResponse {

    private String id;
    private String name;
    private PermissionCategory category;

}
