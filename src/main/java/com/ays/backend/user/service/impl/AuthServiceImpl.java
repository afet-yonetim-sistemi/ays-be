package com.ays.backend.user.service.impl;

import com.ays.backend.user.controller.payload.request.RegisterRequest;
import com.ays.backend.user.controller.payload.response.AuthResponse;
import com.ays.backend.user.model.entities.Organization;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.security.JwtTokenProvider;
import com.ays.backend.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {

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

        if(registerRequest.getOrganizationId() != null) {
            Organization organization = new Organization();
            organization.setId(registerRequest.getOrganizationId());
            user.setOrganization(organization);
        }

        userRepository.save(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .message("success")
                .build();
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
