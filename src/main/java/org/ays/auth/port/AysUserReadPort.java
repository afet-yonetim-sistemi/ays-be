package org.ays.auth.port;

import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserFilter;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPhoneNumber;

import java.util.Optional;

/**
 * Port interface for reading operations related to {@link AysUser}.
 */
public interface AysUserReadPort {

    /**
     * Finds all users with pagination and optional filtering.
     *
     * @param aysPageable the pagination configuration
     * @param filter      the filter for users
     * @return a paginated list of users
     */
    AysPage<AysUser> findAll(AysPageable aysPageable, AysUserFilter filter);

    /**
     * Retrieves a {@link AysUser} by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return An optional containing the {@link AysUser} if found, otherwise empty.
     */
    Optional<AysUser> findById(String id);

    /**
     * Retrieves a {@link AysUser} by its email address.
     *
     * @param emailAddress The email address of the user to retrieve.
     * @return An optional containing the {@link AysUser} if found, otherwise empty.
     */
    Optional<AysUser> findByEmailAddress(String emailAddress);

    /**
     * Finds a user by their phone number, which is a concatenation of country code and line number.
     *
     * @param phoneNumber the concatenated phone number (country code + line number) of the user to be found
     * @return an optional containing the AysUser with the given phone number, or an empty optional if not found
     */
    Optional<AysUser> findByPhoneNumber(AysPhoneNumber phoneNumber);

    /**
     * Finds a user by their password ID.
     *
     * @param passwordId the ID of the password to search for.
     * @return an Optional containing the found user, or empty if no user was found with the given password ID.
     */
    Optional<AysUser> findByPasswordId(String passwordId);

    /**
     * Checks if a user with the given email address exists in the repository.
     *
     * @param emailAddress The email address to check for existence.
     * @return true if a user with the given email address exists, otherwise false.
     */
    boolean existsByEmailAddress(String emailAddress);

    /**
     * Checks if a user with the given phone number exists in the repository.
     *
     * @param phoneNumber The phone number to check for existence.
     * @return true if a user with the given phone number exists, otherwise false.
     */
    boolean existsByPhoneNumber(AysPhoneNumber phoneNumber);


}
