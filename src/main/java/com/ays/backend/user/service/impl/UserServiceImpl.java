package com.ays.backend.user.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.model.entities.Organization;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.service.UserService;
import com.ays.backend.user.service.dto.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserDTO saveUser(SignUpRequest signUpRequest) {
        Organization organization = new Organization();
        organization.setId(signUpRequest.getOrganizationId());
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .userRole(UserRole.getById(signUpRequest.getUserRoleId()))
                .countryCode(signUpRequest.getCountryCode())
                .lineNumber(signUpRequest.getLineNumber())
                .organization(organization)
                .status(UserStatus.getById(signUpRequest.getStatusId()))
                .email(signUpRequest.getEmail())
                .lastLoginDate(LocalDateTime.now())
                .build();

        var createdUser = userRepository.save(user);

        return UserDTO.builder()
                .username(createdUser.getUsername())
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .userRole(UserRole.getById(createdUser.getUserRole().ordinal()))
                .countryCode(createdUser.getCountryCode())
                .lineNumber(createdUser.getLineNumber())
                .organization(createdUser.getOrganization())
                .userStatus(createdUser.getStatus())
                .email(createdUser.getEmail())
                .lastLoginDate(createdUser.getLastLoginDate())
                .build();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findByUserUUID(UUID userUUID) {
        return userRepository.findByUserUUID(userUUID);
    }
}
