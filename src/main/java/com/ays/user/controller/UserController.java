package com.ays.user.controller;

import com.ays.common.controller.dto.response.AysPageResponse;
import com.ays.common.controller.dto.response.AysResponse;
import com.ays.common.model.AysPage;
import com.ays.user.controller.dto.request.UserListRequest;
import com.ays.user.controller.dto.request.UserSaveRequest;
import com.ays.user.controller.dto.request.UserUpdateRequest;
import com.ays.user.controller.dto.response.UserResponse;
import com.ays.user.controller.dto.response.UsersResponse;
import com.ays.user.model.User;
import com.ays.user.model.mapper.UserToUserResponseMapper;
import com.ays.user.model.mapper.UserToUsersResponseMapper;
import com.ays.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * User controller to perform user related api operations.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
class UserController {

    private final UserService userService;

    private final UserToUserResponseMapper userToUserResponseMapper = UserToUserResponseMapper.initialize();
    private final UserToUsersResponseMapper userToUsersResponseMapper = UserToUsersResponseMapper.initialize();


    /**
     * This endpoint allows users to register and create a new account.
     *
     * @param saveRequest A UserSaveRequest object containing the username and password of the new user (required).
     * @return A ResponseEntity containing a SignUpResponse object with the username of the newly created user and
     * the HTTP status code (201 CREATED).
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<Void> saveUser(@RequestBody @Valid UserSaveRequest saveRequest) {
        userService.saveUser(saveRequest);
        return AysResponse.SUCCESS;
    }


    /**
     * This endpoint returns a pageable list of UsersResponse objects.
     *
     * @param listRequest A UserListRequest object containing the page number and page size for the query (optional).
     * @return A AysApiResponse containing a AysPageResponse with UsersResponse and the HTTP status code (200 OK).
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<AysPageResponse<UsersResponse>> getUsers(@RequestBody @Valid UserListRequest listRequest) {
        final AysPage<User> pageOfUsers = userService.getAllUsers(listRequest);

        final AysPageResponse<UsersResponse> pageOfUsersResponse = AysPageResponse.<UsersResponse>builder()
                .of(pageOfUsers)
                .content(userToUsersResponseMapper.map(pageOfUsers.getContent()))
                .build();
        return AysResponse.successOf(pageOfUsersResponse);
    }

    /**
     * This endpoint returns a UserResponse object with the specified ID.
     *
     * @param id A Long representing the ID of the user to retrieve (required).
     * @return A AysApiResponse containing a UserResponse object with the specified ID and the HTTP status code (200 OK).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<UserResponse> getUserById(@PathVariable @UUID String id) {
        final User user = userService.getUserById(id);
        final UserResponse userResponse = userToUserResponseMapper.map(user);
        return AysResponse.successOf(userResponse);
    }


    /**
     * This endpoint returns a Void object by deleting the user softly with the specified ID.
     *
     * @param id A Long representing the ID of the user to retrieve (required).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteSoftUserById(@PathVariable @UUID String id) {
        userService.deleteUser(id);
    }


    /**
     * This endpoint returns a Void object by updating the user with the specified ID.
     *
     * @param updateRequest UserUpdateRequest for updating the user
     * @return A ResponseEntity containing a UserDTO object after implementing the process of updating user
     * with the specified ID by and the HTTP status code (200 OK).
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<Void> updateUserById(@RequestBody UserUpdateRequest updateRequest) {
        userService.updateUser(updateRequest);
        return AysResponse.SUCCESS;
    }
}
