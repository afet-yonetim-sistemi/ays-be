package com.ays.backend.user.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import com.ays.backend.mapper.UserMapper;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.controller.payload.request.UpdateUserRequest;
import com.ays.backend.user.exception.UserNotFoundException;
import com.ays.backend.user.model.entities.Organization;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.service.UserService;
import com.ays.backend.user.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Transactional
    public UserDTO saveUser(SignUpRequest signUpRequest) {

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .userRole(UserRole.ROLE_VOLUNTEER)
                .countryCode(signUpRequest.getCountryCode())
                .lineNumber(signUpRequest.getLineNumber())
                .status(UserStatus.getById(signUpRequest.getStatusId()))
                .email(signUpRequest.getEmail())
                .lastLoginDate(LocalDateTime.now())
                .build();

        if(signUpRequest.getOrganizationId() != null) {
            Organization organization = new Organization();
            organization.setId(signUpRequest.getOrganizationId());
            user.setOrganization(organization);
        }
        var createdUser = userRepository.save(user);

        return UserDTO.builder()
                .username(createdUser.getUsername())
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .userRole(UserRole.getById(createdUser.getUserRole().ordinal()))
                .countryCode(createdUser.getCountryCode())
                .lineNumber(createdUser.getLineNumber())
                .userStatus(createdUser.getStatus())
                .email(createdUser.getEmail())
                .lastLoginDate(createdUser.getLastLoginDate())
                .build();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(
                userMapper::mapUsertoUserDTO
        );
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id).map(
                userMapper::mapUsertoUserDTO
        ).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public UserDTO deleteSoftUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        user.setStatus(UserStatus.PASSIVE);

        var deactivedUser =userRepository.save(user);

        return userMapper.mapUsertoUserDTO(deactivedUser);
    }

    @Override
    public UserDTO updateUserById(Long id, UpdateUserRequest updateUserRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        user = User.builder()
                .username(updateUserRequest.getUsername() != null ? updateUserRequest.getUsername() : user.getUsername())
                .firstName(updateUserRequest.getFirstName() != null ? updateUserRequest.getFirstName() : user.getFirstName())
                .lastName(updateUserRequest.getLastName() != null ? updateUserRequest.getLastName() : user.getLastName())
                .email(updateUserRequest.getEmail() != null ? updateUserRequest.getEmail() : user.getEmail())
                .organization(updateUserRequest.getOrganization() != null ? updateUserRequest.getOrganization() : user.getOrganization())
                .userRole(updateUserRequest.getUserRole() != null ? updateUserRequest.getUserRole() : user.getUserRole())
                .status(updateUserRequest.getUserStatus() != null ? updateUserRequest.getUserStatus() : user.getStatus())
                .countryCode(updateUserRequest.getCountryCode() != 0 ? updateUserRequest.getCountryCode() : user.getCountryCode())
                .lineNumber(updateUserRequest.getLineNumber() != 0 ? updateUserRequest.getLineNumber() : user.getLineNumber())
                .build();

        var updatedUser = userRepository.save(user);

        return userMapper.mapUsertoUserDTO(updatedUser);
    }


}
