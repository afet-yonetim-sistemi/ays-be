package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.User;
import org.ays.auth.model.entity.UserEntity;
import org.ays.auth.model.enums.UserSupportStatus;
import org.ays.auth.model.mapper.UserEntityToUserMapper;
import org.ays.auth.model.request.UserSupportStatusUpdateRequest;
import org.ays.auth.repository.UserRepository;
import org.ays.auth.service.UserSelfService;
import org.ays.auth.util.exception.AysUserNotExistByIdException;
import org.springframework.stereotype.Service;

/**
 * UserSelfServiceImpl is an implementation of the UserSelfService interface.
 * It manages a user's own operations.
 */
@Service
@RequiredArgsConstructor
class UserSelfServiceImpl implements UserSelfService {

    private final UserRepository userRepository;
    private final AysIdentity identity;

    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();

    /**
     * Get the user's self information.
     *
     * @return A User object containing user's self information.
     */
    @Override
    public User getUserSelfInformation() {
        final String userId = identity.getUserId();
        final UserEntity userEntity = userRepository.findById(userId)
                .filter(UserEntity::isActive)
                .orElseThrow(() -> new AysUserNotExistByIdException(userId));
        return userEntityToUserMapper.map(userEntity);
    }

    /**
     * Updates the support status of a user.
     *
     * @param updateRequest the request object containing the updated support status
     */
    @Override
    public void updateUserSupportStatus(UserSupportStatusUpdateRequest updateRequest) {

        final String userId = identity.getUserId();
        final UserSupportStatus userSupportStatus = updateRequest.getSupportStatus();

        final UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AysUserNotExistByIdException(userId));

        userEntity.updateSupportStatus(userSupportStatus);
        userRepository.save(userEntity);

    }

}
