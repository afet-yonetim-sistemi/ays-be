package org.ays.auth.repository;

import org.ays.auth.model.entity.AysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserEntity objects.
 */
public interface AysUserRepository extends JpaRepository<AysUserEntity, String>, JpaSpecificationExecutor<AysUserEntity> {

    /**
     * Finds a user by emailAddress.
     *
     * @param emailAddress the username of the user to be found
     * @return an optional containing the UserEntity with the given username, or an empty optional if not found
     */
    Optional<AysUserEntity> findByEmailAddress(String emailAddress);

    /**
     * Finds a user by their phone number.
     *
     * @param countryCode the country code of the user to check
     * @param lineNumber  the phone number of the user to check
     * @return an optional containing the UserEntity with the given phone number, or an empty optional if not found
     */
    Optional<AysUserEntity> findByCountryCodeAndLineNumber(String countryCode, String lineNumber);

    /**
     * Finds a user entity by the password ID.
     *
     * @param passwordId the ID of the password to search for.
     * @return an Optional containing the found user entity, or empty if no user entity was found with the given password ID.
     */
    Optional<AysUserEntity> findByPasswordId(String passwordId);

    /**
     * Checks if an {@link AysUserEntity} exists with the given email.
     *
     * @param emailAddress the email address of the user to check
     * @return true if an {@link AysUserEntity} exists with the given emailAddress, false otherwise
     */
    boolean existsByEmailAddress(String emailAddress);

    /**
     * Checks if an {@link AysUserEntity} exists with the given country code and phone number.
     *
     * @param countryCode the country code of the user to check
     * @param lineNumber  the phone number of the user to check
     * @return true if an {@link AysUserEntity} exists with the given country code and phone number, false otherwise
     */
    boolean existsByCountryCodeAndLineNumber(String countryCode, String lineNumber);

}
