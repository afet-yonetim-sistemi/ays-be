package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.mapper.AysRoleToRolesResponseMapper;
import org.ays.auth.model.request.AysRoleCreateRequest;
import org.ays.auth.model.request.AysRoleListRequest;
import org.ays.auth.model.response.AysRolesResponse;
import org.ays.auth.service.AysRoleCreateService;
import org.ays.auth.service.AysRoleReadService;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing roles.
 * <p>
 * The {@link  AysRoleController} class provides endpoints for creating and listing roles.
 * It is annotated with {@code @RestController} and {@code @RequestMapping}
 * to define it as a REST controller and to map the base URL for the endpoints.
 * The {@code @RequiredArgsConstructor} annotation is used to generate a constructor
 * with required arguments, in this case, the {@link AysRoleReadService} and {@link AysRoleCreateService}.
 * </p>
 *
 * @see AysRoleReadService
 * @see AysRoleCreateService
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class AysRoleController {

    private final AysRoleReadService roleReadService;
    private final AysRoleCreateService roleCreateService;


    private final AysRoleToRolesResponseMapper roleToRolesResponseMapper = AysRoleToRolesResponseMapper.initialize();


    /**
     * POST /roles : Retrieve all roles based on the provided filtering and pagination criteria.
     * <p>
     * This endpoint handles the retrieval of roles based on the filtering and pagination criteria
     * provided in {@link AysRoleListRequest}. The user must have the 'role:list' authority to access this endpoint.
     * </p>
     *
     * @param listRequest the request object containing filtering and pagination criteria.
     * @return an {@link AysResponse} indicating the success of the operation.
     */
    @PostMapping("/roles")
    @PreAuthorize("hasAnyAuthority('role:list')")
    public AysResponse<AysPageResponse<AysRolesResponse>> findAll(@RequestBody @Valid AysRoleListRequest listRequest) {

        AysPage<AysRole> pageOfRoles = roleReadService.findAll(listRequest);

        final AysPageResponse<AysRolesResponse> pageOfRolesResponse = AysPageResponse.<AysRolesResponse>builder()
                .of(pageOfRoles)
                .content(roleToRolesResponseMapper.map(pageOfRoles.getContent()))
                .build();
        return AysResponse.successOf(pageOfRolesResponse);
    }


    /**
     * POST /role : Create a new role.
     * <p>
     * This endpoint handles the creation of a new role based on the provided {@link AysRoleCreateRequest}.
     * The user must have the 'role:create' authority to access this endpoint.
     * </p>
     *
     * @param createRequest the request object containing the details for the new role.
     * @return an {@link AysResponse} indicating the success of the operation.
     */
    @PostMapping("/role")
    @PreAuthorize("hasAnyAuthority('role:create')")
    public AysResponse<Void> create(@RequestBody @Valid AysRoleCreateRequest createRequest) {
        roleCreateService.create(createRequest);
        return AysResponse.SUCCESS;
    }

}
