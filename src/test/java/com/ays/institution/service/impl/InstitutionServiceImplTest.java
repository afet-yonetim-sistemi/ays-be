package com.ays.institution.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.institution.model.Institution;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.entity.InstitutionEntityBuilder;
import com.ays.institution.model.enums.InstitutionStatus;
import com.ays.institution.model.mapper.InstitutionEntityToInstitutionMapper;
import com.ays.institution.repository.InstitutionRepository;
import org.apache.commons.collections4.CollectionUtils;
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
