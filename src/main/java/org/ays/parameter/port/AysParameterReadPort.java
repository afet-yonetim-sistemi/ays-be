package org.ays.parameter.port;

import org.ays.parameter.model.AysParameter;

import java.util.List;

/**
 * A read port interface for accessing {@link AysParameter} data.
 * Defines methods to retrieve {@link AysParameter} entities based on name or name prefix.
 */
public interface AysParameterReadPort {

    /**
     * Retrieves all {@link AysParameter} entities.
     * <p>
     * This method returns a list of all parameters stored in the data source.
     * </p>
     *
     * @return a list of {@link AysParameter} entities
     */
    List<AysParameter> findAll();

}
