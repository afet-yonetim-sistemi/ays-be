package com.ays.parameter.service.impl;

import com.ays.parameter.model.AysParameter;
import com.ays.parameter.model.mapper.AysParameterEntityToAysParameterMapper;
import com.ays.parameter.repository.AysParameterRepository;
import com.ays.parameter.service.AysParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class AysParameterServiceImpl implements AysParameterService {

    private final AysParameterRepository parameterRepository;
    private final AysParameterEntityToAysParameterMapper aysParameterEntityToAysParameterMapper = AysParameterEntityToAysParameterMapper.initialize();

    @Override
    public Set<AysParameter> getParameters(final String name) {
        return parameterRepository.findByNameStartingWith(name)
                .stream()
                .map(aysParameterEntityToAysParameterMapper::map)
                .collect(Collectors.toSet());
    }

}
