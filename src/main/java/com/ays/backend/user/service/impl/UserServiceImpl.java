package com.ays.backend.user.service.impl;

import com.ays.backend.mapper.UserMapper;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.controller.payload.request.UpdateUserRequest;
import com.ays.backend.user.exception.UserAlreadyExistsException;
import com.ays.backend.user.exception.UserNotFoundException;
import com.ays.backend.user.model.User;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Transactional
    public User saveUser(SignUpRequest signUpRequest) {

        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }

        com.ays.backend.user.model.entities.UserEntity user = com.ays.backend.user.model.entities.UserEntity.builder()
                .organizationId(signUpRequest.getOrganizationId())
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .role(UserRole.ROLE_VOLUNTEER)
                .countryCode(signUpRequest.getCountryCode())
                .lineNumber(signUpRequest.getLineNumber())
                .status(UserStatus.getById(signUpRequest.getStatusId()))
                .email(signUpRequest.getEmail())
                .lastLoginDate(LocalDateTime.now())
                .build();

        var createdUser = userRepository.save(user);

        return User.builder()
                .username(createdUser.getUsername())
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .role(UserRole.getById(createdUser.getRole().ordinal()))
                .countryCode(createdUser.getCountryCode())
                .lineNumber(createdUser.getLineNumber())
                .status(createdUser.getStatus())
                .email(createdUser.getEmail())
                .lastLoginDate(createdUser.getLastLoginDate())
                .build();
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::mapUserEntityToUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findByIdAndStatusNot(id, UserStatus.PASSIVE)
                .map(userMapper::mapUserEntityToUser)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void deleteSoftUserById(Long id) {
        com.ays.backend.user.model.entities.UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        user.deleteUser();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserById(UpdateUserRequest updateUserRequest) {

        final Long id = updateUserRequest.getId();
        com.ays.backend.user.model.entities.UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        user = userMapper.mapUpdateRequestToUser(updateUserRequest, user);
        return userMapper.mapUserEntityToUser(userRepository.save(user));
    }

}
