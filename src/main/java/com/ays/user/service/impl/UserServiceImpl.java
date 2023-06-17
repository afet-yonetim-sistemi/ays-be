package com.ays.user.service.impl;

import com.ays.common.model.AysPage;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserListRequest;
import com.ays.user.model.dto.request.UserUpdateRequest;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.repository.UserRepository;
import com.ays.user.service.UserService;
import com.ays.user.util.exception.AysUserAlreadyActiveException;
import com.ays.user.util.exception.AysUserAlreadyDeletedException;
import com.ays.user.util.exception.AysUserAlreadyPassiveException;
import com.ays.user.util.exception.AysUserNotExistByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserServiceImpl is an implementation of the {@link UserService} interface.
 * It provides methods for managing user data and operations by admin.
 */
@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private static final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();


    /**
     * Retrieves a paginated list of all users based on the provided request.
     *
     * @param listRequest the request object containing pagination parameters
     * @return a paginated list of users
     */
    public AysPage<User> getAllUsers(final UserListRequest listRequest) {
        Page<UserEntity> userEntities = userRepository.findAll(listRequest.toPageable());
        List<User> users = userEntityToUserMapper.map(userEntities.getContent());
        return AysPage.of(
                userEntities,
                users
        );
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID
     * @throws AysUserNotExistByIdException if the user does not exist
     */
    @Override
    public User getUserById(final String id) {
        final UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        return userEntityToUserMapper.map(userEntity);
    }

    /**
     * Updates a user with the specified ID using the provided update request.
     *
     * @param id            the ID of the user to update
     * @param updateRequest the request object containing the updated user data
     * @throws AysUserNotExistByIdException   if the user does not exist
     * @throws AysUserAlreadyActiveException  if the user is already active and the update request sets it to active
     * @throws AysUserAlreadyPassiveException if the user is already passive and the update request sets it to passive
     */
    @Override
    public void updateUser(final String id, final UserUpdateRequest updateRequest) {

        final UserEntity userEntity = userRepository.findById(id)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (UserStatus.ACTIVE.equals(updateRequest.getStatus()) && userEntity.isActive()) {
            throw new AysUserAlreadyActiveException(id);
        }

        if (UserStatus.PASSIVE.equals(updateRequest.getStatus()) && userEntity.isPassive()) {
            throw new AysUserAlreadyPassiveException(id);
        }

        userEntity.updateUser(updateRequest);
        userRepository.save(userEntity);
    }

    /**
     * Deletes a user with the specified ID.
     *
     * @param id the ID of the user to delete
     * @throws AysUserNotExistByIdException   if the user does not exist
     * @throws AysUserAlreadyDeletedException if the user is already deleted
     */
    @Override
    public void deleteUser(final String id) {

        final UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (userEntity.isDeleted()) {
            throw new AysUserAlreadyDeletedException(id);
        }

        userEntity.deleteUser();
        userRepository.save(userEntity);
    }

}
