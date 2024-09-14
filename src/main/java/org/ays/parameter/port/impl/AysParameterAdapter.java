package org.ays.parameter.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.model.entity.AysParameterEntity;
import org.ays.parameter.model.mapper.AysParameterEntityToDomainMapper;
import org.ays.parameter.port.AysParameterReadPort;
import org.ays.parameter.repository.AysParameterRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An adapter class implementing {@link AysParameterReadPort} for accessing {@link AysParameter} entities.
 * This class interacts with the underlying repository to fetch the data and uses a mapper
 * to convert entity objects to domain objects.
 */
@Component
@RequiredArgsConstructor
class AysParameterAdapter implements AysParameterReadPort {

    private final AysParameterRepository parameterRepository;


    private final AysParameterEntityToDomainMapper parameterEntityToParameterMapper = AysParameterEntityToDomainMapper.initialize();


    /**
     * Retrieves a set of {@link AysParameter} entities that have names starting with the given prefix.
     *
     * @param prefixOfName the prefix of the names to search for
     * @return a set of {@link AysParameter} entities with names starting with the given prefix
     */
    @Override
    public Set<AysParameter> findAll(final String prefixOfName) {
        return parameterRepository.findByNameStartingWith(prefixOfName).stream()
                .map(parameterEntityToParameterMapper::map)
                .collect(Collectors.toSet());
    }


    /**
     * Retrieves an {@link AysParameter} entity by its name.
     *
     * @param name the name of the {@link AysParameter} to search for
     * @return an {@link Optional} containing the {@link AysParameter} entity if found, otherwise empty
     */
    @Override
    public Optional<AysParameter> findByName(final String name) {
        final Optional<AysParameterEntity> parameterEntity = parameterRepository.findByName(name);
        return parameterEntity.map(parameterEntityToParameterMapper::map);
    }

}
