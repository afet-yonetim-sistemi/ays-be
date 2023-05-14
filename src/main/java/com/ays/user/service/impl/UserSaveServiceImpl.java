package com.ays.user.service.impl;

import com.ays.auth.model.AysIdentity;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.util.AysRandomUtil;
import com.ays.user.model.User;
import com.ays.user.model.dto.request.UserSaveRequest;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.mapper.UserSaveRequestToUserMapper;
import com.ays.user.repository.UserRepository;
import com.ays.user.service.UserSaveService;
import com.ays.user.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserSaveServiceImpl implements UserSaveService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AysIdentity identity;

    private final UserSaveRequestToUserMapper userSaveRequestToUserMapper = UserSaveRequestToUserMapper.initialize();

    public User saveUser(final UserSaveRequest saveRequest) {

        final List<UserEntity> usersFromDatabase = userRepository.findAll();

        this.checkPhoneNumberExist(saveRequest.getPhoneNumber(), usersFromDatabase);

        final String username = this.generateUsername(usersFromDatabase);

        final String password = String.valueOf(AysRandomUtil.generateNumber(6));

        final UserEntity userEntity = userSaveRequestToUserMapper
                .mapForSaving(
                        saveRequest,
                        identity.getOrganizationId(),
                        username,
                        passwordEncoder.encode(password)
                );

        userRepository.save(userEntity);

        return User.builder()
                .username(username)
                .password(password)
                .build();
    }

    private void checkPhoneNumberExist(final AysPhoneNumber phoneNumber,
                                       final List<UserEntity> usersFromDatabase) {

        final boolean isPhoneNumberExist = usersFromDatabase.stream()
                .filter(userEntity -> userEntity.getCountryCode().equals(phoneNumber.getCountryCode()))
                .anyMatch(userEntity -> userEntity.getLineNumber().equals(phoneNumber.getLineNumber()));

        if (isPhoneNumberExist) {
            throw new AysUserAlreadyExistsByPhoneNumberException(phoneNumber);
        }
    }

    private String generateUsername(final List<UserEntity> usersFromDatabase) {
        final Set<String> usernamesFromDatabase = usersFromDatabase.stream()
                .map(UserEntity::getUsername).collect(Collectors.toSet());

        String username;
        do {
            username = String.valueOf(AysRandomUtil.generateNumber(6));
        } while (usernamesFromDatabase.contains(username));

        return username;
    }
}
