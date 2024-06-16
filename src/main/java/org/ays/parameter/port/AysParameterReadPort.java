package org.ays.parameter.port;

import org.ays.parameter.model.AysParameter;

import java.util.Optional;
import java.util.Set;

/**
 * A read port interface for accessing {@link AysParameter} data.
 * Defines methods to retrieve {@link AysParameter} entities based on name or name prefix.
 */
public interface AysParameterReadPort {

    /**
     * Retrieves a set of {@link AysParameter} entities whose names start with the given prefix.
     *
     * @param prefixOfName the prefix of the names to search for
     * @return a set of {@link AysParameter} entities with names starting with the given prefix
     */
    Set<AysParameter> findAll(String prefixOfName);

    /**
     * Retrieves an {@link AysParameter} entity by its name.
     *
     * @param name the name of the {@link AysParameter} to search for
     * @return an {@link Optional} containing the {@link AysParameter} entity if found, otherwise empty
     */
    Optional<AysParameter> findByName(String name);

}
