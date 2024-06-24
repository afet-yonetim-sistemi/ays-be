package org.ays.auth.port;

import org.ays.auth.model.AysInvalidToken;

import java.util.Optional;

/**
 * A port interface for reading {@link AysInvalidToken}.
 * Defines methods to retrieve {@link AysInvalidToken} instances based on ID or token ID.
 */
public interface AysInvalidTokenReadPort {

    /**
     * Retrieves an {@link AysInvalidToken} by its token ID.
     *
     * @param tokenId The token ID of the {@link AysInvalidToken} to retrieve.
     * @return An {@link Optional} containing the found {@link AysInvalidToken}, or empty if not found.
     */
    Optional<AysInvalidToken> findByTokenId(String tokenId);

}
