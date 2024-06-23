package org.ays.institution.port;

import org.ays.institution.model.Institution;

/**
 * Port interface for saving {@link Institution} entities.
 * <p>
 * This interface defines the contract for saving institution entities, allowing different implementations
 * for persisting institution data. It provides an abstraction layer between the application domain and
 * the data persistence mechanisms, supporting various storage strategies.
 * </p>
 *
 * <p>
 * Implementations of this interface are responsible for handling the persistence of institution objects
 * and may interact with databases, external services, or other storage mechanisms as necessary.
 * </p>
 */
public interface InstitutionSavePort {


    /**
     * Saves the given institution entity.
     * <p>
     * This method takes an {@link Institution} object, persists it using the underlying data store,
     * and returns the saved instance. The saved entity may contain updated state, such as generated IDs
     * or timestamps, depending on the persistence implementation.
     * </p>
     *
     * @param institution the institution entity to be saved
     * @return the saved institution entity with any updates applied by the persistence mechanism
     */
    Institution save(Institution institution);

}
