package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.mapper.AysUserToUsersResponseMapper;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.model.response.AysUsersResponse;
import org.ays.auth.service.AysUserReadService;
import org.ays.auth.service.AysUserUpdateService;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing users.
 * This controller provides endpoints for listing users.
 *
 * @see AysUserReadService
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class AysUserController {

    private final AysUserReadService userReadService;
    private final AysUserUpdateService userUpdateService;


    private final AysUserToUsersResponseMapper userToUsersResponseMapper = AysUserToUsersResponseMapper.initialize();


    /**
     * POST /users : Retrieve all users based on the provided filtering and pagination criteria.
     * <p>
     * This endpoint handles the retrieval of users based on the filtering and pagination criteria
     * provided in {@link AysUserListRequest}. The user must have the 'user:list' authority to access this endpoint.
     * </p>
     *
     * @param request the request object containing filtering and pagination criteria.
     * @return an {@link AysResponse} indicating the success of the operation.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAnyAuthority('user:list')")
    public AysResponse<AysPageResponse<AysUsersResponse>> findAll(@RequestBody @Valid AysUserListRequest request) {

        AysPage<AysUser> pageOfUsers = userReadService.findAll(request);

        final AysPageResponse<AysUsersResponse> pageOfUsersResponse = AysPageResponse.<AysUsersResponse>builder()
                .of(pageOfUsers)
                .content(userToUsersResponseMapper.map(pageOfUsers.getContent()))
                .build();

        return AysResponse.successOf(pageOfUsersResponse);
    }


    /**
     * Update an existing user based on the provided request data.
     * <p>
     * This method is mapped to handle HTTP PUT requests to "/user/{id}". It requires
     * the user to have the 'user:update' authority to access.
     * </p>
     *
     * @param id            The ID of the user to update.
     * @param updateRequest The request object containing updated data for the user.
     * @return An {@link AysResponse} indicating the success of the operation.
     */
    @PutMapping("/user/{id}")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public AysResponse<Void> update(@PathVariable @UUID final String id,
                                    @RequestBody @Valid final AysUserUpdateRequest updateRequest) {

        userUpdateService.update(id, updateRequest);
        return AysResponse.SUCCESS;
    }
}