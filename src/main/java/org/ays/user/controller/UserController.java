package org.ays.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.user.model.User;
import org.ays.user.model.mapper.UserToUserResponseMapper;
import org.ays.user.model.mapper.UserToUserSavedResponseMapper;
import org.ays.user.model.mapper.UserToUsersResponseMapper;
import org.ays.user.model.request.UserListRequest;
import org.ays.user.model.request.UserSaveRequest;
import org.ays.user.model.request.UserUpdateRequest;
import org.ays.user.model.response.UserResponse;
import org.ays.user.model.response.UserSavedResponse;
import org.ays.user.model.response.UsersResponse;
import org.ays.user.service.UserSaveService;
import org.ays.user.service.UserService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller class for managing user-related operations via HTTP requests.
 * This controller handles the CRUD operations for users in the system.
 * The mapping path for this controller is "/api/v1/user".
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
class UserController {

    private final UserService userService;
    private final UserSaveService userSaveService;

    private final UserToUserSavedResponseMapper userToUserSavedResponseMapper = UserToUserSavedResponseMapper.initialize();
    private final UserToUserResponseMapper userToUserResponseMapper = UserToUserResponseMapper.initialize();
    private final UserToUsersResponseMapper userToUsersResponseMapper = UserToUsersResponseMapper.initialize();

    /**
     * Gets a list of users in the system.
     * Requires ADMIN authority.
     *
     * @param listRequest The request object containing the list criteria.
     * @return A response object containing a paginated list of users.
     */
    @PostMapping("/users")
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
     * Gets a user by ID.
     * Requires ADMIN authority.
     *
     * @param id The ID of the user to retrieve.
     * @return A response object containing the retrieved user data.
     */
    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<UserResponse> getUserById(@PathVariable @UUID String id) {
        final User user = userService.getUserById(id);
        final UserResponse userResponse = userToUserResponseMapper.map(user);
        return AysResponse.successOf(userResponse);
    }

    /**
     * Saves a new user to the system.
     * Requires ADMIN authority.
     *
     * @param saveRequest The request object containing the user data to be saved.
     * @return A response object containing the saved user data.
     */
    @PostMapping("/user")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<UserSavedResponse> saveUser(@RequestBody @Valid UserSaveRequest saveRequest) {
        User user = userSaveService.saveUser(saveRequest);
        UserSavedResponse userSavedResponse = userToUserSavedResponseMapper.map(user);
        return AysResponse.successOf(userSavedResponse);
    }

    /**
     * Updates an existing user with the specified user update request.
     * Requires ADMIN authority.
     *
     * @param updateRequest the user update request containing the updated user information
     * @return a success response if the user was updated successfully
     */
    @PutMapping("/user/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<Void> updateUser(@PathVariable @UUID String id,
                                        @RequestBody @Valid UserUpdateRequest updateRequest) {

        userService.updateUser(id, updateRequest);
        return AysResponse.SUCCESS;
    }

    /**
     * Soft-deletes a user by ID.
     * Requires ADMIN authority.
     *
     * @param id The ID of the user to delete.
     * @return A response object indicating a successful deletion.
     */
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<Void> deleteUser(@PathVariable @UUID String id) {
        userService.deleteUser(id);
        return AysResponse.SUCCESS;
    }
}
