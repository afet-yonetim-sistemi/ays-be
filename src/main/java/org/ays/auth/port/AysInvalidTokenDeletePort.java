package org.ays.auth.port;

import org.ays.auth.model.AysInvalidToken;

import java.time.LocalDateTime;

/**
 * A port interface for deleting {@link AysInvalidToken} based on created timestamp.
 * Defines a method to delete all tokens created before a specified expiration threshold.
 */
public interface AysInvalidTokenDeletePort {

    /**
     * Deletes all {@link AysInvalidToken} created before a specified expiration threshold.
     *
     * @param expirationThreshold The timestamp threshold before which all tokens will be deleted.
     */
    void deleteAllByCreatedAtBefore(LocalDateTime expirationThreshold);

}
