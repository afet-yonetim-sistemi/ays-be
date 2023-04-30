package com.ays.admin_user.repository;

import com.ays.admin_user.model.entity.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link AdminUserEntity} in the database.
 */
public interface AdminUserRepository extends JpaRepository<AdminUserEntity, String> {

    /**
     * Finds an {@link AdminUserEntity} by username.
     *
     * @param username the username of the user to find
     * @return an {@link Optional} containing the found {@link AdminUserEntity}, or an empty {@link Optional} if not found
     */
    Optional<AdminUserEntity> findByUsername(String username);

    /**
     * Finds an {@link AdminUserEntity} by email.
     *
     * @param email the email address of the user to find
     * @return an {@link Optional} containing the found {@link AdminUserEntity}, or an empty {@link Optional} if not found
     */
    Optional<AdminUserEntity> findByEmail(String email);

    /**
     * Finds an {@link AdminUserEntity} by country code and phone number.
     *
     * @param countryCode the country code of the user to find
     * @param lineNumber  the phone number of the user to find
     * @return an {@link Optional} containing the found {@link AdminUserEntity}, or an empty {@link Optional} if not found
     */
    Optional<AdminUserEntity> findByCountryCodeAndLineNumber(Integer countryCode, Integer lineNumber);

}
