package org.ays.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.validation.Name;


import java.util.List;
import java.util.Set;

@Getter
@Setter
public class RoleCreateRequest {

    @NotBlank
    private String name;

    @NotEmpty
    private Set<@NotBlank String> permissionIds;
}
