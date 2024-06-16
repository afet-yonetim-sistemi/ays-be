package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.request.RoleCreateRequest;
import org.ays.auth.service.RoleCreateService;
import org.ays.common.model.response.AysResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing roles.
 * <p>
 * The {@code RoleController} class provides endpoints for creating roles.
 * The class is annotated with {@code @RestController} and {@code @RequestMapping}
 * to define it as a REST controller and to map the base URL for the endpoints.
 * The {@code @RequiredArgsConstructor} annotation is used to generate a constructor
 * with required arguments, in this case, the {@link RoleCreateService}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class RoleController {

    private final RoleCreateService roleCreateService;

    /**
     * POST /role : Create a new role.
     * <p>
     * This endpoint handles the creation of a new role based on the provided {@link RoleCreateRequest}.
     * The user must have the 'role:create' authority to access this endpoint.
     * </p>
     *
     * @param createRequest the request object containing the details for the new role.
     * @return an {@link AysResponse} indicating the success of the operation.
     */
    @PostMapping("/role")
    @PreAuthorize("hasAnyAuthority('role:create')")
    public AysResponse<Void> createRole(@RequestBody @Valid RoleCreateRequest createRequest) {
        roleCreateService.create(createRequest);
        return AysResponse.SUCCESS;
    }

}
