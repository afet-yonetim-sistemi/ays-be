package org.ays.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.user.model.dto.request.RoleCreateRequest;
import org.ays.user.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO : Add Javadoc
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class RoleController {

    private final RoleService roleService;

    // TODO : Add Javadoc
    @PostMapping("/role")
    @PreAuthorize("hasAnyAuthority('role:create')")
    public AysResponse<Void> createRole(@RequestBody @Valid RoleCreateRequest createRequest) {
        roleService.create(createRequest);
        return AysResponse.SUCCESS;
    }

}
