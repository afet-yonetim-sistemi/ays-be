package com.ays.backend.user.service;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.user.controller.UserControllerService;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.model.entities.DeviceType;
import com.ays.backend.user.model.entities.PhoneNumber;
import com.ays.backend.user.model.entities.Role;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.DeviceNames;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserControllerServiceTest extends BaseServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private DeviceTypeService deviceTypeService;

    @InjectMocks
    private UserControllerService userControllerService;

    @Test
    public void testCreateUser() {

        // Generate a unique userUUID
        String userUUID = "useruuid";

        Set<String> roles = new HashSet<>(Arrays.asList(UserRole.ROLE_VOLUNTEER.name()));
        Set<String> types = new HashSet<>(Arrays.asList(DeviceNames.DEVICE_1.name()));

        Set<Role> rolesList = new HashSet<>(Arrays.asList(new Role(UserRole.ROLE_VOLUNTEER)));
        Set<DeviceType> deviceNamesList = new HashSet<>(Arrays.asList(new DeviceType(DeviceNames.DEVICE_1)));

        PhoneNumber phoneNumber = PhoneNumber.builder()
                .countryCode(90)
                .lineNumber(123456789)
                .build();

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("testUser")
                .password("password")
                .roles(roles)
                .latitude(10.0)
                .longitude(20.0)
                .phoneNumber(phoneNumber)
                .types(types)
                .status(UserStatus.WAITING.name())
                .build();

        when(roleService.addRoleToUser(signUpRequest.getRoles())).thenReturn(rolesList);
        when(deviceTypeService.addDeviceTypeToUser(signUpRequest.getTypes())).thenReturn(deviceNamesList);

        User user = User.builder()
                .userUUID(userUUID)
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .roles(roleService.addRoleToUser(signUpRequest.getRoles()))
                .latitude(signUpRequest.getLatitude())
                .longitude(signUpRequest.getLongitude())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .types(deviceTypeService.addDeviceTypeToUser(signUpRequest.getTypes()))
                .status(UserStatus.valueOf(signUpRequest.getStatus()))
                .build();


        UserDTO savedUser = UserDTO.builder()
                .userUUID(user.getUserUUID())
                .username(user.getUsername())
                .userStatus(user.getStatus())
                .phoneNumber(user.getPhoneNumber())
                .types(user.getTypes())
                .roles(user.getRoles())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .build();


        when(userService.saveUser(user)).thenReturn(savedUser);

        UserDTO savedUserDTO = userControllerService.createUser(signUpRequest);

        assertEquals(signUpRequest.getUsername(), savedUserDTO.getUsername());
        assertEquals(signUpRequest.getRoles(), savedUserDTO.getRoles());
        assertEquals(signUpRequest.getLatitude(), savedUserDTO.getLatitude(), 0.0);
        assertEquals(signUpRequest.getLongitude(), savedUserDTO.getLongitude(), 0.0);
        assertEquals(signUpRequest.getPhoneNumber(), savedUserDTO.getPhoneNumber());
        assertEquals(signUpRequest.getTypes(), savedUserDTO.getTypes());
        assertEquals(UserStatus.valueOf(signUpRequest.getStatus()), savedUserDTO.getUserStatus());
    }
}
