package org.ays.institution.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.ays.AysUnitTest;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.port.InstitutionReadPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;


class InstitutionServiceImplTest extends AysUnitTest {

    @InjectMocks
    private InstitutionServiceImpl institutionService;

    @Mock
    private InstitutionReadPort institutionReadPort;


    @Test
    void whenActiveInstitutionsExist_thenReturnInstitutions() {

        // When
        InstitutionStatus status = InstitutionStatus.ACTIVE;

        List<Institution> mockActiveInstitutions = List.of(
                new InstitutionBuilder().withValidValues().withStatus(status).build(),
                new InstitutionBuilder().withValidValues().withStatus(status).build()
        );

        Mockito.when(institutionReadPort.findAllByStatusOrderByNameAsc(status))
                .thenReturn(mockActiveInstitutions);

        // Then
        List<Institution> activeInstitutions = institutionService.getSummaryOfActiveInstitutions();

        Assertions.assertNotNull(activeInstitutions);
        Assertions.assertEquals(activeInstitutions.size(), mockActiveInstitutions.size());

        // Verify
        Mockito.verify(institutionReadPort, Mockito.times(1))
                .findAllByStatusOrderByNameAsc(status);
    }

    @Test
    void whenActiveInstitutionsNotExist_thenReturnEmptyList() {

        // When
        InstitutionStatus status = InstitutionStatus.ACTIVE;

        Mockito.when(institutionReadPort.findAllByStatusOrderByNameAsc(status))
                .thenReturn(Collections.emptyList());
        // Then
        List<Institution> institutions = institutionService.getSummaryOfActiveInstitutions();

        Assertions.assertTrue(CollectionUtils.isEmpty(institutions));

        // Verify
        Mockito.verify(institutionReadPort, Mockito.times(1))
                .findAllByStatusOrderByNameAsc(status);

    }

}
