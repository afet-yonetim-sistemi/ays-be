package org.ays.auth.port;

/**
 * Port interface for reading invalid token information.
 */
public interface AysInvalidTokenReadPort {

    /**
     * Checks whether the given token ID is already marked as invalid.
     *
     * @param tokenId The token ID to check.
     * @return true if the token is invalidated, otherwise false.
     */
    boolean exists(String tokenId);

}
