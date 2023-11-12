package com.ays.institution.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.institution.model.Institution;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.entity.InstitutionEntityBuilder;
import com.ays.institution.model.enums.InstitutionStatus;
import com.ays.institution.model.mapper.InstitutionEntityToInstitutionMapper;
import com.ays.institution.repository.InstitutionRepository;
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
    void givenInstitutionEntities_whenInstitutionStatusActive_thenReturnListInstitution() {

        // given
        InstitutionEntity institutionEntity1 = new InstitutionEntityBuilder().withValidFields().build();
        InstitutionEntity institutionEntity2 = new InstitutionEntityBuilder().withValidFields().build();


        List<InstitutionEntity> mockInstitutionEntities = Arrays.asList(
                institutionEntity1,
                institutionEntity2
        );

        List<Institution> mockInstitutionList = institutionEntityToInstitutionMapper.map(mockInstitutionEntities);

        // when
        Mockito.when(institutionRepository.findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE)).thenReturn(mockInstitutionEntities);

        // then
        List<Institution> result = institutionService.getInstitutionsSummary();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), mockInstitutionList.size());

        // verify
        Mockito.verify(institutionRepository, Mockito.times(1)).findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE);

    }

    @Test
    void givenNoInstitutionEntities_whenInstitutionStatusACTIVE_thenReturnEmptyList() {

        // when
        Mockito.when(institutionRepository.findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE))
                .thenReturn(Collections.emptyList());

        // then
        List<Institution> result = institutionService.getInstitutionsSummary();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());

        // verify
        Mockito.verify(institutionRepository, Mockito.times(1)).findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE);

    }

}
