package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.mapper.AysRoleToRoleResponseMapper;
import org.ays.auth.model.mapper.AysRoleToRolesResponseMapper;
import org.ays.auth.model.mapper.AysRoleToRolesSummaryResponseMapper;
import org.ays.auth.model.request.AysRoleCreateRequest;
import org.ays.auth.model.request.AysRoleListRequest;
import org.ays.auth.model.response.AysRoleResponse;
import org.ays.auth.model.response.AysRolesResponse;
import org.ays.auth.model.response.AysRolesSummaryResponse;
import org.ays.auth.service.AysRoleCreateService;
import org.ays.auth.service.AysRoleReadService;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing users.
 * This controller provides endpoints for listing users.
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
    private final AysRoleToRolesSummaryResponseMapper roleToRolesSummaryResponseMapper = AysRoleToRolesSummaryResponseMapper.initialize();
    private final AysRoleToRoleResponseMapper roleToRoleResponseMapper = AysRoleToRoleResponseMapper.initialize();

    /**
     * GET /roles/summary : Retrieve a summary of all roles.
     * <p>
     * This endpoint handles the retrieval of a summary of all roles.
     * The user must have the 'user:create' or 'user:update' authority to access this endpoint.
     * </p>
     *
     * @return an {@link AysResponse} containing a list of {@link AysRolesSummaryResponse} objects,
     *         which represent the summary of roles.
     */
    @GetMapping("/roles/summary")
    @PreAuthorize("hasAnyAuthority('user:create', 'user:update')")
    public AysResponse<List<AysRolesSummaryResponse>> findAllSummary() {

        final List<AysRole> roles = roleReadService.findAll();

        final List<AysRolesSummaryResponse> permissionsResponses = roleToRolesSummaryResponseMapper
                .map(roles);
        return AysResponse.successOf(permissionsResponses);
    }


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
    public AysResponse<AysPageResponse<AysRolesResponse>> findAll(@RequestBody @Valid final AysRoleListRequest listRequest) {

        final AysPage<AysRole> pageOfRoles = roleReadService.findAll(listRequest);

        final AysPageResponse<AysRolesResponse> pageOfRolesResponse = AysPageResponse.<AysRolesResponse>builder()
                .of(pageOfRoles)
                .content(roleToRolesResponseMapper.map(pageOfRoles.getContent()))
                .build();
        return AysResponse.successOf(pageOfRolesResponse);
    }


    /**
     * Handles GET requests for retrieving the details of a role by its ID.
     *
     * @param id the ID of the role to retrieve
     * @return a response entity containing the details of the role
     */
    @GetMapping("/role/{id}")
    @PreAuthorize("hasAuthority('role:detail')")
    public AysResponse<AysRoleResponse> findById(@PathVariable @UUID String id) {
        final AysRole aysRole = roleReadService.findById(id);
        final AysRoleResponse aysRoleResponse = roleToRoleResponseMapper.map(aysRole);
        return AysResponse.successOf(aysRoleResponse);
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
