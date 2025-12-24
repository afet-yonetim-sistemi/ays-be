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
 * Represents a request object for creating a new role.
 * It includes validation annotations to ensure the integrity of the data.
 *
 * <p>
 * This class is typically used as a request object in RESTful APIs for handling requests to create new roles.
 * </p>
 */
@Getter
@Setter
public class AysRoleCreateRequest {

    @RoleNameForm
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    @NotEmpty
    private Set<@NotBlank @UUID String> permissionIds;

}
