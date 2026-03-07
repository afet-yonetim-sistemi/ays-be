package org.ays.parameter.port.adapter;

import org.ays.AysUnitTest;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.model.entity.AysParameterEntity;
import org.ays.parameter.model.entity.AysParameterEntityBuilder;
import org.ays.parameter.repository.AysParameterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

class AysParameterAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysParameterAdapter parameterAdapter;

    @Mock
    private AysParameterRepository parameterRepository;


    @Test
    void whenAllParametersFound_thenReturnParameters() {

        // When
        List<AysParameterEntity> mockParameterEntities = List.of(
                new AysParameterEntityBuilder()
                        .withName("AUTH_ACCESS_TOKEN_EXPIRE_MINUTE")
                        .withDefinition("120")
                        .build(),
                new AysParameterEntityBuilder()
                        .withDefinition("AUTH_REFRESH_TOKEN_EXPIRE_DAY")
                        .withName("1")
                        .build()
        );
        Mockito.when(parameterRepository.findAll())
                .thenReturn(mockParameterEntities);

        // Then
        List<AysParameter> parameterEntities = parameterAdapter.findAll();

        Assertions.assertEquals(mockParameterEntities.size(), parameterEntities.size());

        mockParameterEntities.forEach(mockParameterEntity -> {

            AysParameter parameter = parameterEntities.stream()
                            .filter(parameterEntity -> parameterEntity.getName().equals(mockParameterEntity.getName()))
                            .findFirst()
                            .orElse(null);

                    Assertions.assertNotNull(parameter);
                    Assertions.assertEquals(mockParameterEntity.getName(), parameter.getName());
                    Assertions.assertEquals(mockParameterEntity.getDefinition(), parameter.getDefinition());
                }
        );

        // Verify
        Mockito.verify(parameterRepository, Mockito.times(1))
                .findAll();
    }

}
