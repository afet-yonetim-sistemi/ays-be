package org.ays.institution.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.ays.AbstractUnitTest;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.mapper.InstitutionEntityToInstitutionMapper;
import org.ays.institution.repository.InstitutionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


class InstitutionServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private InstitutionServiceImpl institutionService;

    @Mock
    private InstitutionRepository institutionRepository;

    private final InstitutionEntityToInstitutionMapper institutionEntityToInstitutionMapper = InstitutionEntityToInstitutionMapper.initialize();

    @Test
    void whenActiveInstitutionsExist_thenReturnInstitutions() {

        // When
        List<InstitutionEntity> mockActiveInstitutionEntities = Arrays.asList(
                new InstitutionEntityBuilder().withValidFields().build(),
                new InstitutionEntityBuilder().withValidFields().build()
        );

        Mockito.when(institutionRepository.findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE))
                .thenReturn(mockActiveInstitutionEntities);

        // Then
        List<Institution> mockActiveInstitutions = institutionEntityToInstitutionMapper
                .map(mockActiveInstitutionEntities);
        List<Institution> activeInstitutions = institutionService.getSummaryOfActiveInstitutions();

        Assertions.assertNotNull(activeInstitutions);
        Assertions.assertEquals(activeInstitutions.size(), mockActiveInstitutions.size());

        Mockito.verify(institutionRepository, Mockito.times(1))
                .findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE);

    }

    @Test
    void whenActiveInstitutionsNotExist_thenReturnEmptyList() {

        // When
        Mockito.when(institutionRepository.findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE))
                .thenReturn(Collections.emptyList());
        // Then
        List<Institution> institutions = institutionService.getSummaryOfActiveInstitutions();

        Assertions.assertTrue(CollectionUtils.isEmpty(institutions));

        Mockito.verify(institutionRepository, Mockito.times(1))
                .findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE);

    }

}
