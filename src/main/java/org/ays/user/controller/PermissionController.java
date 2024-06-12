package org.ays.user.controller;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.response.AysResponse;
import org.ays.user.model.Permission;
import org.ays.user.model.dto.response.PermissionsResponse;
import org.ays.user.model.mapper.PermissionToPermissionsResponseMapper;
import org.ays.user.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing permissions.
 * This controller provides endpoints for retrieving permissions.
 * <p>
 * The class is annotated with {@code @RestController} and {@code @RequestMapping}
 * to define it as a REST controller and to map the base URL for the endpoints.
 * The {@code @RequiredArgsConstructor} annotation is used to generate a constructor
 * with required arguments, in this case, the {@link PermissionService}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class PermissionController {

    private final PermissionService permissionService;

    private final PermissionToPermissionsResponseMapper permissionToPermissionsResponseMapper = PermissionToPermissionsResponseMapper.initialize();

    /**
     * GET /permissions : Get all permissions.
     * <p>
     * This endpoint returns a list of all permissions. The user must have the 'role:create'
     * or 'role:update' authority to access this endpoint.
     * </p>
     *
     * @return {@link AysResponse} containing a list of {@link PermissionsResponse}.
     */
    @GetMapping("/permissions")
    @PreAuthorize("hasAnyAuthority('role:create', 'role:update')")
    public AysResponse<List<PermissionsResponse>> findAll() {

        final List<Permission> permissions = permissionService.findAll();

        final List<PermissionsResponse> permissionsResponses = permissionToPermissionsResponseMapper
                .map(permissions);
        return AysResponse.successOf(permissionsResponses);
    }

}
