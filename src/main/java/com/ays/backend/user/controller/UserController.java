package com.ays.backend.user.controller;

import com.ays.backend.user.model.EStatus;
import com.ays.backend.user.model.Role;
import com.ays.backend.user.model.Type;
import com.ays.backend.user.model.User;
import com.ays.backend.user.payload.request.SignUpRequest;
import com.ays.backend.user.payload.response.MessageResponse;
import com.ays.backend.user.payload.response.SignUpResponse;
import com.ays.backend.user.service.RoleService;
import com.ays.backend.user.service.TypeService;
import com.ays.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final TypeService typeService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {

        String username = signUpRequest.getUsername();
        Set<String> strRoles = signUpRequest.getRoles();

        if(userService.existsByUsername(username)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        Set<Role> userRoles = roleService.addRoleToUser(strRoles);

        Set<Type> userTypes = typeService.addTypeToUser(signUpRequest.getTypes());

        User user = User.builder()
                .userUUID(UUID.randomUUID())
                .username(username)
                .password(signUpRequest.getPassword())
                .roles(userRoles)
                .latitude(signUpRequest.getLatitude())
                .longitude(signUpRequest.getLongitude())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .types(userTypes)
                .status(EStatus.valueOf(signUpRequest.getStatus()))
                .build();


        User createdUser = userService.saveUser(user);

        SignUpResponse signUpResponse = new SignUpResponse(createdUser.getUserUUID());
        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }
}
