package org.ays.institution.port.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.ays.AysUnitTest;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.mapper.InstitutionEntityToDomainMapper;
import org.ays.institution.repository.InstitutionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

class InstitutionAdapterTest extends AysUnitTest {

    @InjectMocks
    private InstitutionAdapter institutionAdapter;

    @Mock
    private InstitutionRepository institutionRepository;


    private final InstitutionEntityToDomainMapper institutionEntityToDomainMapper = InstitutionEntityToDomainMapper.initialize();


    @Test
    void givenActiveInstitutionStatus_whenActiveInstitutionsExist_thenReturnInstitutions() {
        // Given
        InstitutionStatus mockStatus = InstitutionStatus.ACTIVE;

        // When
        List<InstitutionEntity> mockActiveInstitutionEntities = List.of(
                new InstitutionEntityBuilder().withValidValues().withStatus(mockStatus).build(),
                new InstitutionEntityBuilder().withValidValues().withStatus(mockStatus).build()
        );
        Mockito.when(institutionRepository.findAllByStatusOrderByNameAsc(mockStatus))
                .thenReturn(mockActiveInstitutionEntities);

        // Then
        List<Institution> mockActiveInstitutions = institutionEntityToDomainMapper
                .map(mockActiveInstitutionEntities);
        List<Institution> activeInstitutions = institutionAdapter.findAllByStatusOrderByNameAsc(mockStatus);

        Assertions.assertNotNull(activeInstitutions);
        Assertions.assertEquals(activeInstitutions.size(), mockActiveInstitutions.size());

        // Verify
        Mockito.verify(institutionRepository, Mockito.times(1))
                .findAllByStatusOrderByNameAsc(mockStatus);
    }


    @Test
    void givenActiveInstitutionStatus_whenActiveInstitutionsNotExist_thenReturnEmptyList() {
        // Given
        InstitutionStatus status = InstitutionStatus.ACTIVE;

        // When
        Mockito.when(institutionRepository.findAllByStatusOrderByNameAsc(status))
                .thenReturn(Collections.emptyList());

        // Then
        List<Institution> institutions = institutionAdapter.findAllByStatusOrderByNameAsc(status);

        Assertions.assertTrue(CollectionUtils.isEmpty(institutions));

        Mockito.verify(institutionRepository, Mockito.times(1))
                .findAllByStatusOrderByNameAsc(status);

    }


    @Test
    void givenValidId_whenActiveInstitutinExist_thenReturnTrue() {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        InstitutionStatus mockStatus = InstitutionStatus.ACTIVE;
        Mockito.when(institutionRepository.existsByIdAndStatus(mockId, mockStatus))
                .thenReturn(true);

        // Then
        boolean isInstitutionExist = institutionAdapter.existsByIdAndIsStatusActive(mockId);

        Assertions.assertTrue(isInstitutionExist);

        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsByIdAndStatus(mockId, mockStatus);
    }

    @Test
    void givenValidId_whenActiveInstitutionNotExist_thenReturnFalse() {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        InstitutionStatus mockStatus = InstitutionStatus.ACTIVE;
        Mockito.when(institutionRepository.existsByIdAndStatus(mockId, mockStatus))
                .thenReturn(false);

        // Then
        boolean isInstitutionExist = institutionAdapter.existsByIdAndIsStatusActive(mockId);

        Assertions.assertFalse(isInstitutionExist);

        Mockito.verify(institutionRepository, Mockito.times(1))
                .existsByIdAndStatus(mockId, mockStatus);
    }


}
