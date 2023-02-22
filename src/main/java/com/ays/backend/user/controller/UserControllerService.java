package com.ays.backend.user.controller;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.model.entities.DeviceType;
import com.ays.backend.user.model.entities.Role;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.service.DeviceTypeService;
import com.ays.backend.user.service.RoleService;
import com.ays.backend.user.service.UserService;
import com.ays.backend.user.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * User controller service to perform multiple use case operations in a single business service for the user api.
 */
@Service
@RequiredArgsConstructor
public class UserControllerService {
    private final UserService userService;
    private final RoleService roleService;
    private final DeviceTypeService deviceTypeService;
    private final Supplier<UUID> uuidSupplier = UUID::randomUUID;

    public UserDTO createUser(SignUpRequest signUpRequest) {
        Set<Role> userRoles = roleService.getUserRoles(signUpRequest.getRoles());

        Set<DeviceType> userTypes = deviceTypeService.addDeviceTypeToUser(signUpRequest.getTypes());

        User user = User.builder()
                .userUUID(uuidSupplier.get().toString())
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .roles(userRoles)
                .latitude(signUpRequest.getLatitude())
                .longitude(signUpRequest.getLongitude())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .types(userTypes)
                .status(UserStatus.valueOf(signUpRequest.getStatus()))
                .build();


        return userService.saveUser(user);
    }

    public boolean existsByUsername(String username) {
        return userService.existsByUsername(username);
    }
}
