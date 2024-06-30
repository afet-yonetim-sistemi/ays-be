package org.ays.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.mapper.AysUserToUsersResponseMapper;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.response.AysUserListResponse;
import org.ays.auth.service.AysUserReadService;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users.
 * <p>
 * The {@link AysUserController} class provides endpoints for listing users.
 * It is annotated with {@code @RestController} and {@code @RequestMapping}
 * to define it as a REST controller and to map the base URL for the endpoints.
 * The {@code @RequiredArgsConstructor} annotation is used to generate a constructor
 * with required arguments, in this case, the {@link AysUserReadService}.
 * </p>
 *
 * @see AysUserReadService
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class AysUserController {

    private final AysUserReadService userReadService;


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
    public AysResponse<AysPageResponse<AysUserListResponse>> findAll(@RequestBody @Valid AysUserListRequest request) {

        AysPage<AysUser> pageOfAysUsers = userReadService.findAll(request);

        final AysPageResponse<AysUserListResponse> pageOfUsersResponse = AysPageResponse.<AysUserListResponse>builder()
                .of(pageOfAysUsers)
                .content(userToUsersResponseMapper.map(pageOfAysUsers.getContent()))
                .build();

        return AysResponse.successOf(pageOfUsersResponse);
    }

}