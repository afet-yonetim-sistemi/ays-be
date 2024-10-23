package org.ays.parameter.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.parameter.exception.AysParameterNotExistException;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.port.AysParameterReadPort;
import org.ays.parameter.service.AysParameterService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service implementation for retrieving {@link AysParameter} entities.
 * Provides methods to fetch {@link AysParameter} entities based on name or name prefix.
 */
@Service
@RequiredArgsConstructor
class AysParameterServiceImpl implements AysParameterService {

    private final AysParameterReadPort parameterReadPort;


    /**
     * Retrieves a set of {@link AysParameter} entities that have names starting with the given prefix.
     *
     * @param prefixOfName the prefix of the names to search for
     * @return a set of {@link AysParameter} entities with names starting with the given prefix
     */
    @Override
    public Set<AysParameter> findAll(final String prefixOfName) {
        return parameterReadPort.findAll(prefixOfName);
    }


    /**
     * Retrieves an {@link AysParameter} that has the given name.
     * Throws {@link AysParameterNotExistException} if no parameter with the given name is found.
     *
     * @param name the name of the {@link AysParameter} to search for
     * @return the {@link AysParameter} with the given name
     * @throws AysParameterNotExistException if the parameter with the given name does not exist
     */
    @Override
    public AysParameter findByName(final String name) {
        return parameterReadPort.findByName(name)
                .orElseThrow(() -> new AysParameterNotExistException(name));
    }

}
