package com.ays.backend.user.service.impl;

import com.ays.backend.mapper.UserMapper;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.exception.OrganizationNotFoundException;
import com.ays.backend.user.exception.UserAlreadyExistsException;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.repository.OrganizationRepository;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.service.AuthService;
import com.ays.backend.user.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {

    //private final AuthenticationManager authenticationManager;

    //private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public UserDTO register(AdminRegisterRequest registerRequest) {

        if (organizationRepository.existsById(registerRequest.getOrganizationId())) {
            throw new OrganizationNotFoundException("Error: Organization Not Found!");
        }

        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerRequest.getUsername()))) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }

        User user = User.from(registerRequest, passwordEncoder);

        var registeredUser = userRepository.save(user);

        return userMapper.mapUsertoUserDTO(registeredUser);

    }

    /*public void login(){

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .message("success")
                .build();


    }*/
}
