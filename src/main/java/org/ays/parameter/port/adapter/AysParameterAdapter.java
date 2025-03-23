package org.ays.parameter.port.adapter;

import lombok.RequiredArgsConstructor;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.model.entity.AysParameterEntity;
import org.ays.parameter.model.mapper.AysParameterEntityToDomainMapper;
import org.ays.parameter.port.AysParameterReadPort;
import org.ays.parameter.repository.AysParameterRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * An adapter class implementing {@link AysParameterReadPort} for accessing {@link AysParameter} entities.
 * This class interacts with the underlying repository to fetch the data and uses a mapper
 * to convert entity objects to domain objects.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AysParameterAdapter implements AysParameterReadPort {

    private final AysParameterRepository parameterRepository;


    private final AysParameterEntityToDomainMapper parameterEntityToParameterMapper = AysParameterEntityToDomainMapper.initialize();


    /**
     * Retrieves all {@link AysParameter} entities from the repository and maps them to domain objects.
     * <p>
     * This method fetches all parameter entities stored in the underlying repository, converts them
     * into {@link AysParameter} objects using the provided mapper, and returns them as a list.
     * </p>
     *
     * @return a list of {@link AysParameter} entities
     */
    @Override
    public List<AysParameter> findAll() {
        return parameterRepository.findAll().stream()
                .map(parameterEntityToParameterMapper::map)
                .toList();
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
