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

    // todo javadoc
    AysPage<AysUser> findAll(AysPageable aysPageable, AysUserFilter filter);

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
