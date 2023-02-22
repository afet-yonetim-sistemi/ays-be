package com.ays.backend.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

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
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserControllerServiceTest extends BaseServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private DeviceTypeService deviceTypeService;

    @Mock
    private Supplier<UUID> uuidSupplier;

    @InjectMocks
    private UserControllerService userControllerService;

    @Test
    void shouldCreateUser() {
        ReflectionTestUtils.setField(userControllerService, "uuidSupplier", uuidSupplier);
        Set<String> roles = new HashSet<>(List.of(UserRole.ROLE_VOLUNTEER.name()));
        Set<String> types = new HashSet<>(List.of(DeviceNames.DEVICE_1.name()));
        Set<Role> rolesList = new HashSet<>(List.of(new Role(UserRole.ROLE_VOLUNTEER)));
        Set<DeviceType> deviceNamesList = new HashSet<>(List.of(new DeviceType(DeviceNames.DEVICE_1)));

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

        when(roleService.getUserRoles(signUpRequest.getRoles())).thenReturn(rolesList);
        when(deviceTypeService.addDeviceTypeToUser(signUpRequest.getTypes())).thenReturn(deviceNamesList);
        var uuid = "7df6b4a2-b28d-11ed-afa1-0242ac120002";
        when(uuidSupplier.get()).thenReturn(UUID.fromString(uuid));

        User user = User.builder()
                .userUUID(uuid)
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .roles(roleService.getUserRoles(signUpRequest.getRoles()))
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
        var savedRoles = savedUserDTO.getRoles().stream().map(role -> role.getName().getValue()).toList();
        assertTrue(signUpRequest.getRoles().containsAll(savedRoles));
        assertEquals(signUpRequest.getLatitude(), savedUserDTO.getLatitude(), 0.0);
        assertEquals(signUpRequest.getLongitude(), savedUserDTO.getLongitude(), 0.0);
        assertEquals(signUpRequest.getPhoneNumber(), savedUserDTO.getPhoneNumber());
        var savedTypes = savedUserDTO.getTypes().stream().map(deviceType -> deviceType.getName().name()).toList();
        assertTrue(signUpRequest.getTypes().containsAll(savedTypes));
        assertEquals(UserStatus.valueOf(signUpRequest.getStatus()), savedUserDTO.getUserStatus());
    }
}
