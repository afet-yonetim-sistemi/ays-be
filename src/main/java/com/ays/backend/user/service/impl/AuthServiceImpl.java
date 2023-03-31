package com.ays.backend.user.service.impl;

import com.ays.backend.mapper.UserMapper;
import com.ays.backend.user.controller.payload.request.AdminLoginRequest;
import com.ays.backend.user.controller.payload.request.AdminRefreshTokenRequest;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.exception.UserAlreadyExistsException;
import com.ays.backend.user.exception.UserNotFoundException;
import com.ays.backend.user.model.Token;
import com.ays.backend.user.model.User;
import com.ays.backend.user.model.entities.UserEntity;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.security.JwtTokenProvider;
import com.ays.backend.user.security.JwtUserDetails;
import com.ays.backend.user.service.AuthService;
import com.ays.backend.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

//    private final OrganizationRepository organizationRepository;

    private final RefreshTokenService refreshTokenService;

    @Value("${ays.token.expires-in}")
    private long EXPIRES_IN;

    @Override
    @Transactional
    public User register(AdminRegisterRequest registerRequest) {


//        if (organizationRepository.existsById(registerRequest.getOrganizationId())) {
//            throw new OrganizationNotFoundException("Error: Organization Not Found!");
//        }

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }

        UserEntity user = UserEntity.from(registerRequest, passwordEncoder);

        return userMapper.mapUserEntityToUser(userRepository.save(user));
    }

    @Override
    @Transactional
    public Token login(AdminLoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return jwtTokenProvider.generateJwtToken(auth);

    }

    @Override
    public Token refreshToken(AdminRefreshTokenRequest refreshTokenRequest) {

        String refreshToken = refreshTokenRequest.getRefreshToken();
        String username = jwtTokenProvider.getUserNameFromJwtToken(refreshToken);

        var userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Error: User Not Found"));

        JwtUserDetails jwtUserDetails = new JwtUserDetails(userEntity);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwtUserDetails.getUsername(), jwtUserDetails.getPassword());

        return jwtTokenProvider.generateJwtToken(auth);

    }


}
