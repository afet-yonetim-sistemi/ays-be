package org.ays.auth.controller;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysPermission;
import org.ays.auth.model.mapper.AysPermissionToPermissionsResponseMapper;
import org.ays.auth.model.response.AysPermissionsResponse;
import org.ays.auth.service.AysPermissionService;
import org.ays.common.model.response.AysResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * REST controller for managing permissions.
 * This controller provides endpoints for retrieving permissions.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class AysPermissionController {

    private final AysPermissionService permissionService;

    private final AysPermissionToPermissionsResponseMapper permissionToPermissionsResponseMapper = AysPermissionToPermissionsResponseMapper.initialize();

    /**
     * Retrieves all permissions.
     * <p>
     * This endpoint returns a list of all permissions. The user must have the 'role:create'
     * or 'role:update' authority to access this endpoint.
     * </p>
     *
     * @return {@link AysResponse} containing a list of {@link AysPermissionsResponse}.
     */
    @GetMapping("/permissions")
    @PreAuthorize("hasAnyAuthority('role:create', 'role:update')")
    public AysResponse<List<AysPermissionsResponse>> findAll() {

        final Set<AysPermission> permissions = permissionService.findAll();

        final List<AysPermissionsResponse> permissionsResponses = permissionToPermissionsResponseMapper
                .map(permissions);
        return AysResponse.successOf(permissionsResponses);
    }

}
