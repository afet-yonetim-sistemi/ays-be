package com.ays.backend.user.service;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.service.dto.UserDTO;
import com.ays.backend.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUser() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("testUser")
                .password("password")
                .userRoleId(1)
                .organizationId(123L)
                .countryCode(90)
                .lineNumber(123456789)
                .statusId(1)
                .build();

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .userRole(UserRole.getById(signUpRequest.getUserRoleId()))
                .status(UserStatus.getById(signUpRequest.getStatusId()))
                .countryCode(signUpRequest.getCountryCode())
                .lineNumber(signUpRequest.getLineNumber())
                .build();


        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserDTO savedUserDTO = userService.saveUser(signUpRequest);

        // then
        assertEquals(signUpRequest.getUsername(), savedUserDTO.getUsername());
        assertEquals(signUpRequest.getUserRoleId(), savedUserDTO.getUserRole().ordinal());
        assertEquals(signUpRequest.getCountryCode(), savedUserDTO.getCountryCode());
        assertEquals(signUpRequest.getLineNumber(), savedUserDTO.getLineNumber());
        assertEquals(signUpRequest.getStatusId(), savedUserDTO.getUserStatus().ordinal());
    }
}
