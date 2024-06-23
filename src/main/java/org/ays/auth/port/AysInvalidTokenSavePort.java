package org.ays.auth.port;

import org.ays.auth.model.AysInvalidToken;

import java.util.Set;

/**
 * A port interface for saving {@link AysInvalidToken} entities.
 * Defines a method to persist multiple {@link AysInvalidToken} instances.
 */
public interface AysInvalidTokenSavePort {

    /**
     * Persists multiple {@link AysInvalidToken} instances.
     *
     * @param invalidTokens The set of {@link AysInvalidToken} instances to be saved.
     */
    void saveAll(Set<AysInvalidToken> invalidTokens);

}
