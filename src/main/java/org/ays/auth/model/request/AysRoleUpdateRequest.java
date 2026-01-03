package org.ays.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.validation.RoleNameForm;
import org.hibernate.validator.constraints.UUID;

import java.util.Set;

/**
 * Request object for updating a role.
 * This class is used to encapsulate the data needed to update a role, including the role name and permission IDs.
 */
@Getter
@Setter
public class AysRoleUpdateRequest {

    @RoleNameForm
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    @NotEmpty
    private Set<@NotBlank @UUID String> permissionIds;

}
