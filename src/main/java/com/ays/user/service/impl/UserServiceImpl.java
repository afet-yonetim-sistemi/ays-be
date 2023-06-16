package com.ays.user.service.impl;

import com.ays.auth.model.AysIdentity;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysSpecification;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AysIdentity identity;

    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();

    public AysPage<User> getAllUsers(final UserListRequest listRequest) {

        final Map<String, Object> filter = Map.of("institutionId", identity.getInstitutionId());
        final Specification<UserEntity> specification = AysSpecification.<UserEntity>builder().and(filter);

        Page<UserEntity> userEntities = userRepository.findAll(specification, listRequest.toPageable());
        List<User> users = userEntityToUserMapper.map(userEntities.getContent());
        return AysPage.of(
                userEntities,
                users
        );
    }

    @Override
    public User getUserById(final String id) {
        final UserEntity userEntity = userRepository.findByIdAndInstitutionId(id, identity.getInstitutionId())
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        return userEntityToUserMapper.map(userEntity);
    }

    @Override
    public void updateUser(final String id, final UserUpdateRequest updateRequest) {

        final UserEntity userEntity = userRepository.findByIdAndInstitutionId(id, identity.getInstitutionId())
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

    @Override
    public void deleteUser(final String id) {

        final UserEntity userEntity = userRepository.findByIdAndInstitutionId(id, identity.getInstitutionId())
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        if (userEntity.isDeleted()) {
            throw new AysUserAlreadyDeletedException(id);
        }

        userEntity.deleteUser();
        userRepository.save(userEntity);
    }

}
