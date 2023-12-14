package com.ays.user.service.impl;

import com.ays.auth.model.AysIdentity;
import com.ays.common.model.dto.request.AysPhoneNumberRequest;
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

/**
 * UserSaveServiceImpl is an implementation of the {@link UserSaveService} interface.
 * It provides methods for saving user data and performing related operations by admin.
 */
@Service
@RequiredArgsConstructor
class UserSaveServiceImpl implements UserSaveService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AysIdentity identity;

    private static final UserSaveRequestToUserMapper userSaveRequestToUserMapper = UserSaveRequestToUserMapper.initialize();

    /**
     * Saves a new user based on the provided save request.
     *
     * @param saveRequest the request object containing user data to be saved
     * @return the saved user
     * @throws AysUserAlreadyExistsByPhoneNumberException if a user with the same phone number already exists
     */
    public User saveUser(final UserSaveRequest saveRequest) {

        final List<UserEntity> usersFromDatabase = userRepository.findAll();

        this.checkPhoneNumberExist(saveRequest.getPhoneNumber(), usersFromDatabase);

        final String username = this.generateUsername(usersFromDatabase);

        final String password = String.valueOf(AysRandomUtil.generateNumber(6));

        final UserEntity userEntity = userSaveRequestToUserMapper
                .mapForSaving(
                        saveRequest,
                        identity.getInstitutionId(),
                        username,
                        passwordEncoder.encode(password)
                );

        userRepository.save(userEntity);

        return User.builder()
                .username(username)
                .password(password)
                .build();
    }

    /**
     * Checks if a phone number already exists in the database.
     *
     * @param phoneNumber       the phone number to check
     * @param usersFromDatabase the list of user entities from the database
     * @throws AysUserAlreadyExistsByPhoneNumberException if a user with the same phone number already exists
     */
    private void checkPhoneNumberExist(final AysPhoneNumberRequest phoneNumber,
                                       final List<UserEntity> usersFromDatabase) {

        final boolean isPhoneNumberExist = usersFromDatabase.stream()
                .filter(userEntity -> userEntity.getCountryCode().equals(phoneNumber.getCountryCode()))
                .anyMatch(userEntity -> userEntity.getLineNumber().equals(phoneNumber.getLineNumber()));

        if (isPhoneNumberExist) {
            throw new AysUserAlreadyExistsByPhoneNumberException(phoneNumber);
        }
    }

    /**
     * Generates a unique username that does not already exist in the database.
     *
     * @param usersFromDatabase the list of user entities from the database
     * @return a unique username
     */
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
