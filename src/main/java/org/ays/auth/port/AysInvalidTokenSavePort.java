package org.ays.auth.port;

import org.ays.auth.model.AysInvalidToken;

import java.util.Set;

/**
 * Port interface for saving invalid tokens.
 * <p>
 * This interface defines a contract for persisting a set of {@link AysInvalidToken} objects to the underlying storage.
 * Implementations should handle the batch saving process and return the set of saved invalid tokens.
 * </p>
 */
public interface AysInvalidTokenSavePort {

    /**
     * Persists a set of {@link AysInvalidToken} objects.
     *
     * @param invalidTokens the set of invalid tokens to be saved
     * @return a {@link Set} of {@link AysInvalidToken} objects that have been successfully saved
     */
    Set<AysInvalidToken> saveAll(Set<AysInvalidToken> invalidTokens);

}
