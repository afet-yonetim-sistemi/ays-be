package org.ays.auth.port.adapter;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.auth.model.mapper.AysUserLoginAttemptToEntityMapper;
import org.ays.auth.port.UserLoginAttemptSavePort;
import org.ays.auth.repository.UserLoginAttemptRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adapter for saving {@link AysUser.LoginAttempt} entries without persisting the entire user aggregate.
 */
@Component
@Transactional
@RequiredArgsConstructor
class UserLoginAttemptSaveAdapter implements UserLoginAttemptSavePort {

    private final UserLoginAttemptRepository userLoginAttemptRepository;

    private final AysUserLoginAttemptToEntityMapper userLoginAttemptMapper = AysUserLoginAttemptToEntityMapper.initialize();

    /**
     * Saves the given {@link AysUser} login attempt to the repository.
     *
     * @param userId       the id of user
     * @param loginAttempt the login attempt information to save
     */
    @Override
    public void save(final String userId,
                     final AysUser.LoginAttempt loginAttempt) {

        final AysUserEntity.LoginAttemptEntity loginAttemptEntity = userLoginAttemptMapper.map(loginAttempt);
        loginAttemptEntity.setUser(AysUserEntity.builder().id(userId).build());
        userLoginAttemptRepository.save(loginAttemptEntity);
    }

}
