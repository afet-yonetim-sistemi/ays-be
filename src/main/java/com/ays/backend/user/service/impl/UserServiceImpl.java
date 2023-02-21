package com.ays.backend.user.service.impl;

import java.util.Optional;
import java.util.UUID;

import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.service.UserService;
import com.ays.backend.user.service.dto.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserDTO saveUser(User user) {
        var createdUser = userRepository.save(user);

        return UserDTO.builder()
                .username(createdUser.getUsername())
                .userUUID(createdUser.getUserUUID())
                .phoneNumber(createdUser.getPhoneNumber())
                .userStatus(createdUser.getStatus())
                .types(createdUser.getTypes())
                .roles(createdUser.getRoles())
                .latitude(createdUser.getLatitude())
                .longitude(createdUser.getLongitude())
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
