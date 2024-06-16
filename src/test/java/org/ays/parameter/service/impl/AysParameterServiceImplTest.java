package org.ays.parameter.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.model.AysParameterBuilder;
import org.ays.parameter.port.AysParameterReadPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;
import java.util.stream.Collectors;

class AysParameterServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AysParameterServiceImpl parameterService;


    @MockBean
    private AysParameterReadPort parameterReadPort;


    @Test
    void testFindAll() {
        // Given
        String prefixOfName = "AUTH_TOKEN";

        // When
        Set<AysParameter> mockParameters = AysParameterBuilder.getParameters().stream()
                .filter(p -> p.getName().startsWith(prefixOfName))
                .collect(Collectors.toSet());

        Mockito.when(parameterReadPort.findAll(prefixOfName))
                .thenReturn(mockParameters);

        // Then
        Set<AysParameter> parameters = parameterReadPort.findAll(prefixOfName);

        Assertions.assertEquals(mockParameters, parameters);

    }

}