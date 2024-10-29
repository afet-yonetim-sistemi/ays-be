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

import java.util.Optional;
import java.util.Set;

class AysParameterAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysParameterAdapter parameterAdapter;

    @Mock
    private AysParameterRepository parameterRepository;


    @Test
    void givenValidPrefixOfParameterName_whenParametersFound_thenReturnParameters() {
        // Given
        String mockPrefixOfName = "AUTH_";

        // When
        Set<AysParameterEntity> mockParameterEntities = Set.of(
                new AysParameterEntityBuilder()
                        .withName("AUTH_ACCESS_TOKEN_EXPIRE_MINUTE")
                        .withDefinition("120")
                        .build(),
                new AysParameterEntityBuilder()
                        .withDefinition("AUTH_REFRESH_TOKEN_EXPIRE_DAY")
                        .withName("1")
                        .build()
        );

        Mockito.when(parameterRepository.findByNameStartingWith(mockPrefixOfName))
                .thenReturn(mockParameterEntities);

        // Then
        Set<AysParameter> parameterEntities = parameterAdapter.findAll(mockPrefixOfName);

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
        Mockito.verify(parameterRepository)
                .findByNameStartingWith(mockPrefixOfName);

    }


    @Test
    void givenValidParameterName_whenParameterFound_thenReturnParameter() {
        // Given
        String mockName = "AUTH_ACCESS_TOKEN_EXPIRE_MINUTE";

        // When
        AysParameterEntity mockParameterEntity = new AysParameterEntityBuilder()
                .withName("AUTH_ACCESS_TOKEN_EXPIRE_MINUTE")
                .withDefinition("120")
                .build();

        Mockito.when(parameterRepository.findByName(mockName))
                .thenReturn(Optional.of(mockParameterEntity));

        // Then
        Optional<AysParameter> parameter = parameterAdapter.findByName(mockName);

        Assertions.assertTrue(parameter.isPresent());
        Assertions.assertEquals(mockParameterEntity.getName(), parameter.get().getName());
        Assertions.assertEquals(mockParameterEntity.getDefinition(), parameter.get().getDefinition());

        // Verify
        Mockito.verify(parameterRepository, Mockito.times(1))
                .findByName(mockName);
    }

}
