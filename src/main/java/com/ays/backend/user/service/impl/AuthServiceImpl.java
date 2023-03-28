package com.ays.backend.user.service.impl;

import com.ays.backend.mapper.UserMapper;
import com.ays.backend.user.controller.payload.request.AdminLoginRequest;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.exception.OrganizationNotFoundException;
import com.ays.backend.user.exception.UserAlreadyExistsException;
import com.ays.backend.user.model.entities.RefreshToken;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.repository.OrganizationRepository;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.security.JwtTokenProvider;
import com.ays.backend.user.security.JwtUserDetails;
import com.ays.backend.user.service.AuthService;
import com.ays.backend.user.service.RefreshTokenService;
import com.ays.backend.user.service.dto.UserDTO;
import com.ays.backend.user.service.dto.UserTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final OrganizationRepository organizationRepository;

    private final RefreshTokenService refreshTokenService;

    @Value("${ays.token.expires-in}")
    private long EXPIRES_IN;

    @Override
    @Transactional
    public UserDTO register(AdminRegisterRequest registerRequest) {

        if (Boolean.FALSE.equals(organizationRepository.existsById(registerRequest.getOrganizationId()))) {
            throw new OrganizationNotFoundException("Error: Organization Not Found!");
        }

        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerRequest.getUsername()))) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }

        User user = User.from(registerRequest, passwordEncoder);

        var registeredUser = userRepository.save(user);

        return userMapper.mapUsertoUserDTO(registeredUser);

    }

    @Override
    @Transactional
    public UserTokenDTO login(AdminLoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        String accessToken = jwtTokenProvider.generateJwtToken(auth);

        Set<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toSet());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        Date expiryDate = new Date(new Date().getTime() + EXPIRES_IN);

        return UserTokenDTO.builder()
                .username(userDetails.getUsername())
                .accessToken("Bearer " + accessToken)
                .roles(roles)
                .refreshToken(refreshToken.getToken())
                .message("success")
                .expireDate(expiryDate.getTime())
                .build();

    }

}
