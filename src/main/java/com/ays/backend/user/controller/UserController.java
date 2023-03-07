package com.ays.backend.user.controller;

import com.ays.backend.user.controller.payload.request.PaginationRequest;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.controller.payload.request.UpdateUserRequest;
import com.ays.backend.user.controller.payload.response.SignUpResponse;
import com.ays.backend.user.exception.UserAlreadyExistsException;
import com.ays.backend.user.service.UserService;
import com.ays.backend.user.service.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User controller to perform user related api operations.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * This endpoint allows users to register and create a new account.
     *
     * @param signUpRequest A SignUpRequest object containing the username and password of the new user (required).
     * @return A ResponseEntity containing a SignUpResponse object with the username of the newly created user and
     *         the HTTP status code (201 CREATED).
     * @throws UserAlreadyExistsException If the username provided in the request body already exists in the database.
     */
    @PostMapping
    public ResponseEntity<SignUpResponse> registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userService.existsByUsername(signUpRequest.getUsername()))) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }

        var createdUser = userService.saveUser(signUpRequest);

        SignUpResponse signUpResponse = new SignUpResponse(createdUser.getUsername());
        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }

    /**
     * This endpoint returns a pageable list of UserDTO objects.
     *
     * @param paginationRequest A PaginationRequest object containing the page number and page size for the query (optional).
     * @return A ResponseEntity containing a Page object with UserDTOs and the HTTP status code (200 OK).
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize());
        return new ResponseEntity<>(userService.getAllUsers(pageable), HttpStatus.OK);
    }

    /**
     * This endpoint returns a UserDTO object with the specified ID.
     *
     * @param id A Long representing the ID of the user to retrieve (required).
     * @return A ResponseEntity containing a UserDTO object with the specified ID and the HTTP status code (200 OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


    /**
     * This endpoint returns a UserDTO object by deleting the user softly with the specified ID.
     *
     * @param id A Long representing the ID of the user to retrieve (required).
     * @return A ResponseEntity containing a UserDTO object after implementing the process of deleting user softly
     *         with the specified ID and the HTTP status code (200 OK).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteSoftUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteSoftUserById(id), HttpStatus.OK);
    }

    /**
     * This endpoint returns a UserDTO object by updating the user with the specified ID.
     *
     * @param updateUserRequest UpdateUserRequest for updating the user
     * @return A ResponseEntity containing a UserDTO object after implementing the process of updating user
     *         with the specified ID by and the HTTP status code (200 OK).
     */
    @PutMapping
    public ResponseEntity<UserDTO> updateUserById(@RequestBody UpdateUserRequest updateUserRequest) {
        return new ResponseEntity<>(userService.updateUserById(updateUserRequest), HttpStatus.OK);
    }
}
