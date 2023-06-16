package com.ays.user.service.impl;

import com.ays.auth.model.AysIdentity;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.repository.UserRepository;
import com.ays.user.service.UserSelfService;
import com.ays.user.util.exception.AysUserNotExistByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSelfServiceImpl implements UserSelfService {

    private final UserRepository userRepository;

    private final AysIdentity identity;

    @Override
    public void updateUserSupportStatus(UserSupportStatusUpdateRequest updateRequest) {

        String userId = identity.getUserId();

        final UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AysUserNotExistByIdException(userId));

        userEntity.updateSupportStatus(updateRequest.getSupportStatus());
        userRepository.save(userEntity);

    }
}
