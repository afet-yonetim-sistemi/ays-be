package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.mapper.AysUserToResponseMapper;
import org.ays.auth.model.mapper.AysUserToUsersResponseMapper;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.response.AysUserResponse;
import org.ays.auth.model.response.AysUsersResponse;
import org.ays.auth.service.AysUserReadService;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    private final AysUserToUsersResponseMapper userToUsersResponseMapper = AysUserToUsersResponseMapper.initialize();
    private final AysUserToResponseMapper userToResponseMapper = AysUserToResponseMapper.initialize();


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
     * GET /user/{id} : Retrieve the details of a user by its ID.
     * <p>
     * This endpoint handles the retrieval of a user by its ID. The user must have the 'user:detail'
     * authority to access this endpoint.
     * </p>
     *
     * @param id The ID of the user to retrieve.
     * @return An {@link AysResponse} containing the {@link AysUserResponse} if the user is found.
     */

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('user:detail')")
    public AysResponse<AysUserResponse> findById(@PathVariable @UUID final String id) {
        final AysUser user = userReadService.findById(id);
        final AysUserResponse userResponse = userToResponseMapper.map(user);
        return AysResponse.successOf(userResponse);
    }

}
