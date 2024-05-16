package org.ays.user.repository;

import org.ays.user.model.entity.UserEntityV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserEntity objects.
 */
public interface UserRepositoryV2 extends JpaRepository<UserEntityV2, String>, JpaSpecificationExecutor<UserEntityV2> {

    /**
     * Finds a user by emailAddress.
     *
     * @param emailAddress the username of the user to be found
     * @return an optional containing the UserEntity with the given username, or an empty optional if not found
     */
    Optional<UserEntityV2> findByEmailAddress(String emailAddress);

    /**
     * Retrieves an optional UserEntity based on the provided user ID and institution ID.
     *
     * @param id            The ID of the user to retrieve.
     * @param institutionId The ID of the institution associated with the user.
     * @return An optional UserEntity that matches the specified ID and institution ID, or an empty optional if not found.
     */
    Optional<UserEntityV2> findByIdAndInstitutionId(String id, String institutionId);

    /**
     * Checks if an {@link UserEntityV2} exists with the given email.
     *
     * @param emailAddress the email address of the user to check
     * @return true if an {@link UserEntityV2} exists with the given emailAddress, false otherwise
     */
    boolean existsByEmailAddress(String emailAddress);

    /**
     * Checks if an {@link UserEntityV2} exists with the given country code and phone number.
     *
     * @param countryCode the country code of the user to check
     * @param lineNumber  the phone number of the user to check
     * @return true if an {@link UserEntityV2} exists with the given country code and phone number, false otherwise
     */
    boolean existsByCountryCodeAndLineNumber(String countryCode, String lineNumber);

}
