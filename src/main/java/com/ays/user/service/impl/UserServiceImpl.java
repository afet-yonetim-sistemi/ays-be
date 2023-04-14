package com.ays.user.service.impl;

import com.ays.common.model.AysPage;
import com.ays.user.controller.dto.request.UserListRequest;
import com.ays.user.controller.dto.request.UserSaveRequest;
import com.ays.user.controller.dto.request.UserUpdateRequest;
import com.ays.user.model.User;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.repository.UserRepository;
import com.ays.user.service.UserService;
import com.ays.user.util.exception.AysUserNotExistByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();


    @Transactional
    public void saveUser(UserSaveRequest saveRequest) {
//
//        if (userRepository.findByUsername(saveRequest.getUsername()).isPresent()) {
//            throw new UserAlreadyExistsException("Error: Username is already taken!");
//        }
//
//        UserEntity user = UserEntity.builder()
//                .organizationId(saveRequest.getOrganizationId())
//                .username(saveRequest.getUsername())
//                .password(saveRequest.getPassword())
//                .firstName(saveRequest.getFirstName())
//                .lastName(saveRequest.getLastName())
//                .role(UserRole.ROLE_VOLUNTEER)
//                .countryCode(saveRequest.getCountryCode())
//                .lineNumber(saveRequest.getLineNumber())
//                .status(UserStatus.getById(saveRequest.getStatusId()))
//                .email(saveRequest.getEmail())
//                .lastLoginDate(LocalDateTime.now())
//                .build();
//
//        var createdUser = userRepository.save(user);
//
//        return User.builder()
//                .username(createdUser.getUsername())
//                .firstName(createdUser.getFirstName())
//                .lastName(createdUser.getLastName())
//                .role(UserRole.getById(createdUser.getRole().ordinal()))
//                .countryCode(createdUser.getCountryCode())
//                .lineNumber(createdUser.getLineNumber())
//                .status(createdUser.getStatus())
//                .email(createdUser.getEmail())
//                .lastLoginDate(createdUser.getLastLoginDate())
//                .build();
    }

    public AysPage<User> getAllUsers(UserListRequest listRequest) {
        Page<UserEntity> userEntities = userRepository.findAll(listRequest.toPageable());
        List<User> users = userEntityToUserMapper.map(userEntities.getContent());
        return AysPage.of(
                userEntities,
                users
        );
    }

    @Override
    public User getUserById(String id) {
        final UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        return userEntityToUserMapper.map(userEntity);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        final UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        userEntity.deleteUser();
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateRequest updateRequest) {

        final String id = updateRequest.getId();
        final UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        // userEntity.update(); TODO : user update method must be written
        userRepository.save(userEntity);
    }

}
