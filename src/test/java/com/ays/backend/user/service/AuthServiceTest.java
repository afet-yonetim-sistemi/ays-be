package com.ays.backend.user.service;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.mapper.UserMapper;
import com.ays.backend.user.controller.payload.request.AdminLoginRequest;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.controller.payload.response.AuthResponse;
import com.ays.backend.user.model.entities.RefreshToken;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.entities.UserBuilder;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.repository.OrganizationRepository;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.security.JwtTokenProvider;
import com.ays.backend.user.security.JwtUserDetails;
import com.ays.backend.user.service.dto.UserDTO;
import com.ays.backend.user.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AuthServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;


    @Test
    void shouldRegister() {

        // Given
        AdminRegisterRequest registerRequest = new UserBuilder().getRegisterRequest();

        User user = new UserBuilder()
                .withUserStatus(UserStatus.getById(registerRequest.getStatusValue()))
                .withRegisterRequest(registerRequest, passwordEncoder).build();

        UserDTO userDto = UserDTO.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userRole(user.getUserRole())
                .countryCode(user.getCountryCode())
                .lineNumber(user.getLineNumber())
                .userStatus(user.getStatus())
                .email(user.getEmail())
                .lastLoginDate(user.getLastLoginDate())
                .build();

        // when
        when(organizationRepository.existsById(registerRequest.getOrganizationId())).thenReturn(true);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.mapUsertoUserDTO(any(User.class))).thenReturn(userDto);

        var registerUser = authService.register(registerRequest);

        // then
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getUserRole().ordinal(), userDto.getUserRole().ordinal());
        assertEquals(user.getCountryCode(), userDto.getCountryCode());
        assertEquals(user.getLineNumber(), userDto.getLineNumber());
        assertEquals(user.getStatus(), userDto.getUserStatus());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastLoginDate(), userDto.getLastLoginDate());
    }

    @Test
    void shouldLogin() {

        // Given
        AdminLoginRequest loginRequest = new UserBuilder().getLoginRequest();


        User user = User.builder()
                .username(loginRequest.getUsername())
                .password(loginRequest.getPassword())
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        user.setId(1L);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());


        JwtUserDetails jwtUserDetails = new JwtUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(jwtUserDetails, null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();


        AuthResponse authResponse = AuthResponse.builder()
                .expireDate(new Date().getTime() + 120000)
                .refreshToken("refreshToken")
                .message("success")
                .accessToken("access-token")
                .roles(List.of("ADMIN", "SUPER_ADMIN"))
                .username("adminUsername")
                .build();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(authResponse.getRefreshToken())
                .build();

        // when
        when(authenticationManager.authenticate(eq(authToken)))
                .thenReturn(auth);
        when(jwtTokenProvider.generateJwtToken(eq(auth))).thenReturn(authResponse.getAccessToken());
        when(refreshTokenService.createRefreshToken(eq(userDetails.getId()))).thenReturn(refreshToken);

        AuthResponse authResponseActual = authService.login(loginRequest);

        // then
        assertThat(authResponse).isNotNull();
        assertEquals(authResponseActual.getAccessToken().substring(7), authResponse.getAccessToken());
        assertEquals(authResponseActual.getUsername(), authResponse.getUsername());
        assertEquals(authResponseActual.getRefreshToken(), authResponse.getRefreshToken());
        assertEquals(authResponseActual.getMessage(), authResponse.getMessage());

    }
}
