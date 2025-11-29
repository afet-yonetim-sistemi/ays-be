package org.ays.institution.port.adapter;

import org.apache.commons.collections4.CollectionUtils;
import org.ays.AysUnitTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPageableBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.institution.model.InstitutionFilter;
import org.ays.institution.model.InstitutionFilterBuilder;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.mapper.InstitutionEntityToDomainMapper;
import org.ays.institution.model.mapper.InstitutionToEntityMapper;
import org.ays.institution.repository.InstitutionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Set;

class InstitutionAdapterTest extends AysUnitTest {

    @InjectMocks
    private InstitutionAdapter institutionAdapter;

    @Mock
    private InstitutionRepository institutionRepository;


    private final InstitutionEntityToDomainMapper institutionEntityToDomainMapper = InstitutionEntityToDomainMapper.initialize();
    private final InstitutionToEntityMapper institutionToEntityMapper = InstitutionToEntityMapper.initialize();

    @Test
    @SuppressWarnings("unchecked")
    void givenValidAysPageableWithoutFilter_whenInstitutionsFound_thenReturnInstitutionsPage() {

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withValidValues()
                .withoutOrders()
                .build();
        InstitutionFilter mockFilter = new InstitutionFilterBuilder().build();

        // When
        List<InstitutionEntity> mockEntities = List.of(
                new InstitutionEntityBuilder()
                        .withValidValues()
                        .build()
        );
        Page<InstitutionEntity> mockEntitiesPage = new PageImpl<>(mockEntities);
        Mockito.when(institutionRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockEntitiesPage);

        List<Institution> mockInstitutions = institutionEntityToDomainMapper.map(mockEntities);
        AysPage<Institution> mockInstitutionsPage = AysPageBuilder.from(mockInstitutions, mockAysPageable, mockFilter);

        // Then
        AysPage<Institution> institutionsPage = institutionAdapter.findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockInstitutionsPage, institutionsPage);

        // Verify
        Mockito.verify(institutionRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenValidAysPageableAndFilter_whenInstitutionsFound_thenReturnInstitutionsPage() {

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withValidValues()
                .withoutOrders()
                .build();
        InstitutionFilter mockFilter = new InstitutionFilterBuilder()
                .withName("Dernek")
                .withStatuses(Set.of(InstitutionStatus.ACTIVE))
                .build();

        // When
        List<InstitutionEntity> mockInstitutionEntities = List.of(
                new InstitutionEntityBuilder()
                        .withValidValues()
                        .withStatus(InstitutionStatus.ACTIVE)
                        .build()
        );
        Page<InstitutionEntity> mockInstitutionEntitiesPage = new PageImpl<>(mockInstitutionEntities);
        Mockito.when(institutionRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockInstitutionEntitiesPage);

        List<Institution> mockInstitutions = institutionEntityToDomainMapper.map(mockInstitutionEntities);
        AysPage<Institution> mockInstitutionsPage = AysPageBuilder.from(mockInstitutions, mockAysPageable, mockFilter);

        // Then
        AysPage<Institution> institutionsPage = institutionAdapter.findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockInstitutionsPage, institutionsPage);

        // Verify
        Mockito.verify(institutionRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

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
        InstitutionStatus mockStatus = InstitutionStatus.ACTIVE;

        // When
        Mockito.when(institutionRepository.findAllByStatusOrderByNameAsc(mockStatus))
                .thenReturn(Collections.emptyList());

        // Then
        List<Institution> institutions = institutionAdapter.findAllByStatusOrderByNameAsc(mockStatus);

        Assertions.assertTrue(CollectionUtils.isEmpty(institutions));

        // Verify
        Mockito.verify(institutionRepository, Mockito.times(1))
                .findAllByStatusOrderByNameAsc(mockStatus);
    }


    @Test
    void givenValidId_whenActiveInstitutionExist_thenReturnTrue() {
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


    @Test
    void givenValidInstitution_whenInstitutionSaved_thenReturnInstitutionFromDatabase() {

        // Given
        Institution mockInstitution = new InstitutionBuilder()
                .withValidValues()
                .withoutId()
                .build();

        // When
        InstitutionEntity mockInstitutionEntity = institutionToEntityMapper.map(mockInstitution);
        String mockId = AysRandomUtil.generateUUID();
        mockInstitutionEntity.setId(mockId);
        Mockito.when(institutionRepository.save(Mockito.any(InstitutionEntity.class)))
                .thenReturn(mockInstitutionEntity);

        mockInstitution.setId(mockId);

        // Then
        Institution institution = institutionAdapter.save(mockInstitution);

        Assertions.assertNotNull(institution);
        Assertions.assertEquals(mockInstitution.getId(), institution.getId());

        // Verify
        Mockito.verify(institutionRepository, Mockito.times(1))
                .save(Mockito.any(InstitutionEntity.class));
    }


}
