package com.ays.backend.user.service;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.user.controller.payload.request.RegisterRequest;
import com.ays.backend.user.controller.payload.response.AuthResponse;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.security.JwtTokenProvider;
import com.ays.backend.user.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Test
    public void shouldRegister() {

        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testadmin")
                .password("testadmin")
                .countryCode("1")
                .lineNumber("1234567890")
                .firstName("First Name Admin")
                .lastName("Last Name Admin")
                .email("testadmin@afet.com")
                .organizationId(1L)
                .build();

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .userRole(UserRole.ROLE_ADMIN)
                .countryCode(Integer.parseInt(registerRequest.getCountryCode()))
                .lineNumber(Integer.parseInt(registerRequest.getLineNumber()))
                .email(registerRequest.getEmail())
                .lastLoginDate(LocalDateTime.now())
                .build();

        // when
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword()));
        //when(SecurityContextHolder.getContext()).thenReturn(any(SecurityContext.class));
        //when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(jwtTokenProvider.generateJwtToken(any(Authentication.class))).thenReturn("test_token");

        SecurityContextHolder.setContext(securityContext);

        AuthResponse authResponse = authService.register(registerRequest);

        // then
        assertEquals(authResponse.getAccessToken(), "test_token");
        assertEquals(authResponse.getMessage(), "success");
    }
}
