package org.ays.parameter.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.model.AysParameterBuilder;
import org.ays.parameter.port.AysParameterReadPort;
import org.ays.parameter.util.exception.AysParameterNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

class AysParameterServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AysParameterServiceImpl parameterService;

    @Mock
    private AysParameterReadPort parameterReadPort;


    @Test
    void givenValidPrefixOfParameterName_whenParametersFound_thenReturnParameters() {
        // Given
        String mockPrefixOfName = "AUTH_";

        // When
        Set<AysParameter> mockParameters = Set.of(
                new AysParameterBuilder()
                        .withName("AUTH_ACCESS_TOKEN_EXPIRE_MINUTE")
                        .withDefinition("120")
                        .build(),
                new AysParameterBuilder()
                        .withDefinition("AUTH_REFRESH_TOKEN_EXPIRE_DAY")
                        .withName("1")
                        .build()
        );

        Mockito.when(parameterReadPort.findAll(mockPrefixOfName))
                .thenReturn(mockParameters);

        // Then
        Set<AysParameter> parameters = parameterService.getParameters(mockPrefixOfName);

        Assertions.assertEquals(mockParameters, parameters);

        // Verify
        Mockito.verify(parameterReadPort)
                .findAll(mockPrefixOfName);
    }


    @Test
    void givenValidParameterName_whenParameterFound_thenReturnParameter() {
        // Given
        String mockName = "AUTH_ACCESS_TOKEN_EXPIRE_MINUTE";

        // When
        AysParameter mockParameter = new AysParameterBuilder()
                .withName("AUTH_ACCESS_TOKEN_EXPIRE_MINUTE")
                .withDefinition("120")
                .build();

        Mockito.when(parameterReadPort.findByName(mockName))
                .thenReturn(Optional.of(mockParameter));

        // Then
        AysParameter parameter = parameterService.getParameter(mockName);

        Assertions.assertEquals(mockParameter.getName(), parameter.getName());
        Assertions.assertEquals(mockParameter.getDefinition(), parameter.getDefinition());

        // Verify
        Mockito.verify(parameterReadPort, Mockito.times(1))
                .findByName(mockName);
    }

    @Test
    void givenValidParameterName_whenParameterNotFound_thenThrowAysParameterNotFoundException() {
        // Given
        String mockName = "AUTH_ACCESS_TOKEN";

        // When
        Mockito.when(parameterReadPort.findByName(mockName))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysParameterNotExistException.class,
                () -> parameterService.getParameter(mockName)
        );

        // Verify
        Mockito.verify(parameterReadPort, Mockito.times(1))
                .findByName(mockName);
    }

}