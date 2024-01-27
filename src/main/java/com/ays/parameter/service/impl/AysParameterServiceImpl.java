package com.ays.parameter.service.impl;

import com.ays.parameter.model.AysParameter;
import com.ays.parameter.model.entity.AysParameterEntity;
import com.ays.parameter.model.mapper.AysParameterEntityToAysParameterMapper;
import com.ays.parameter.repository.AysParameterRepository;
import com.ays.parameter.service.AysParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for retrieving AysParameter entities.
 */
@Service
@RequiredArgsConstructor
class AysParameterServiceImpl implements AysParameterService {

    private final AysParameterRepository parameterRepository;
    private final AysParameterEntityToAysParameterMapper aysParameterEntityToAysParameterMapper = AysParameterEntityToAysParameterMapper.initialize();

    /**
     * Retrieves a set of AysParameter entities that have a name starting with the given prefix.
     *
     * @param prefixOfName the prefix to search for
     * @return a set of AysParameter entities
     */
    @Override
    public Set<AysParameter> getParameters(final String prefixOfName) {
        return parameterRepository.findByNameStartingWith(prefixOfName)
                .stream()
                .map(aysParameterEntityToAysParameterMapper::map)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves an AysParameter entity that has the given name.
     *
     * @param name the name to search for
     * @return an AysParameter entity
     */
    @Override
    public AysParameter getParameter(final String name) {
        final AysParameterEntity parameterEntity = parameterRepository.findByName(name);
        return aysParameterEntityToAysParameterMapper.map(parameterEntity);
    }

}
