package org.ays.auth.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserFilter;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.auth.model.mapper.AysUserEntityToDomainMapper;
import org.ays.auth.model.mapper.AysUserToEntityMapper;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.repository.AysUserRepository;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPhoneNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Adapter class implementing both {@link AysUserReadPort} and {@link AysUserSavePort} interfaces.
 * Retrieves {@link AysUser} entities from the repository, saves {@link AysUser} entities to the database,
 * and maps between domain models and entity models.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AysUserAdapter implements AysUserReadPort, AysUserSavePort {

    private final AysUserRepository userRepository;


    private final AysUserToEntityMapper userToEntityMapper = AysUserToEntityMapper.initialize();
    private final AysUserEntityToDomainMapper userEntityToDomainMapper = AysUserEntityToDomainMapper.initialize();


    /**
     * Finds all users with pagination and optional filtering.
     * <p>
     * This method uses the provided {@link AysPageable} for pagination and {@link AysUserFilter} for filtering.
     * It returns a paginated list of {@link AysUser} domain models.
     * </p>
     *
     * @param aysPageable the pagination configuration
     * @param filter      the filter for users
     * @return a paginated list of users
     */
    @Override
    public AysPage<AysUser> findAll(AysPageable aysPageable, AysUserFilter filter) {

        final Pageable pageable = aysPageable.toPageable();

        final Specification<AysUserEntity> specification = filter.toSpecification();

        final Page<AysUserEntity> userEntitysPage = userRepository.findAll(specification, pageable);

        final List<AysUser> users = userEntityToDomainMapper.map(userEntitysPage.getContent());

        return AysPage.of(filter, userEntitysPage, users);
    }


    /**
     * Retrieves an {@link AysUser} by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return An optional containing the {@link AysUser} if found, otherwise empty.
     */
    @Override
    public Optional<AysUser> findById(final String id) {
        Optional<AysUserEntity> userEntity = userRepository.findById(id);
        return userEntity.map(userEntityToDomainMapper::map);
    }


    /**
     * Retrieves an {@link AysUser} by its email address.
     *
     * @param emailAddress The email address of the user to retrieve.
     * @return An optional containing the {@link AysUser} if found, otherwise empty.
     */
    @Override
    public Optional<AysUser> findByEmailAddress(final String emailAddress) {
        Optional<AysUserEntity> userEntity = userRepository.findByEmailAddress(emailAddress);
        return userEntity.map(userEntityToDomainMapper::map);
    }


    /**
     * Checks if a user with the given email address exists in the repository.
     *
     * @param emailAddress The email address to check for existence.
     * @return true if a user with the given email address exists, otherwise false.
     */
    @Override
    public boolean existsByEmailAddress(final String emailAddress) {
        return userRepository.existsByEmailAddress(emailAddress);
    }


    /**
     * Finds a user by their phone number, which is a concatenation of country code and line number.
     *
     * @param phoneNumber the concatenated phone number (country code + line number) of the user to be found
     * @return an optional containing the {@link AysUser} with the given phone number, or an empty optional if not found
     */
    @Override
    public Optional<AysUser> findByPhoneNumber(String phoneNumber) {
        Optional<AysUserEntity> userEntity = userRepository.findByPhoneNumber(phoneNumber);
        return userEntity.map(userEntityToDomainMapper::map);
    }


    /**
     * Checks if a user with the given phone number exists in the repository.
     *
     * @param phoneNumber The phone number to check for existence.
     * @return true if a user with the given phone number exists, otherwise false.
     */
    @Override
    public boolean existsByPhoneNumber(final AysPhoneNumber phoneNumber) {
        return userRepository.existsByCountryCodeAndLineNumber(
                phoneNumber.getCountryCode(),
                phoneNumber.getLineNumber()
        );
    }


    /**
     * Saves an {@link AysUser} to the database.
     *
     * @param user The {@link AysUser} to save.
     * @return The saved {@link AysUser} after persistence.
     */
    @Override
    @Transactional
    public AysUser save(final AysUser user) {

        final AysUserEntity userEntity = userToEntityMapper.map(user);

        if (user.getPassword() != null) {
            userEntity.getPassword().setUser(userEntity);
        }

        if (user.getLoginAttempt() != null) {
            userEntity.getLoginAttempt().setUser(userEntity);
        }

        userRepository.save(userEntity);
        return userEntityToDomainMapper.map(userEntity);
    }

}
