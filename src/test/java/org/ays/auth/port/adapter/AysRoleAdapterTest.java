package org.ays.auth.port.adapter;

import org.apache.commons.collections4.CollectionUtils;
import org.ays.AysUnitTest;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.AysRoleFilter;
import org.ays.auth.model.AysRoleFilterBuilder;
import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.entity.AysRoleEntityBuilder;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.mapper.AysRoleEntityToDomainMapper;
import org.ays.auth.model.mapper.AysRoleToEntityMapper;
import org.ays.auth.repository.AysRoleRepository;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPageableBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class AysRoleAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysRoleAdapter roleAdapter;

    @Mock
    private AysRoleRepository roleRepository;


    private final AysRoleToEntityMapper roleToEntityMapper = AysRoleToEntityMapper.initialize();
    private final AysRoleEntityToDomainMapper roleEntityToDomainMapper = AysRoleEntityToDomainMapper.initialize();


    @Test
    @SuppressWarnings("unchecked")
    void givenValidAysPageableWithoutFilter_whenApplicationsFound_thenReturnApplicationsPage() {

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withValidValues()
                .withoutOrders()
                .build();
        AysRoleFilter mockFilter = new AysRoleFilterBuilder().build();

        // When
        List<AysRoleEntity> mockEntities = List.of(
                new AysRoleEntityBuilder()
                        .withValidValues()
                        .build()
        );
        Page<AysRoleEntity> mockEntitiesPage = new PageImpl<>(mockEntities);
        Mockito.when(roleRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockEntitiesPage);

        List<AysRole> mockRoles = roleEntityToDomainMapper.map(mockEntities);
        AysPage<AysRole> mockRolesPage = AysPageBuilder.from(mockRoles, mockAysPageable, mockFilter);

        // Then
        AysPage<AysRole> rolesPage = roleAdapter.findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockRolesPage, rolesPage);

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenValidAysPageableAndFilter_whenRolesFound_thenReturnRolesPage() {

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withValidValues()
                .withoutOrders()
                .build();
        AysRoleFilter mockFilter = new AysRoleFilterBuilder()
                .withStatuses(Set.of(AysRoleStatus.ACTIVE))
                .build();

        // When
        List<AysRoleEntity> mockRoleEntities = List.of(
                new AysRoleEntityBuilder()
                        .withValidValues()
                        .withStatus(AysRoleStatus.ACTIVE)
                        .build()
        );
        Page<AysRoleEntity> mockRoleEntitiesPage = new PageImpl<>(mockRoleEntities);
        Mockito.when(roleRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockRoleEntitiesPage);

        List<AysRole> mockRoles = roleEntityToDomainMapper.map(mockRoleEntities);
        AysPage<AysRole> mockRolesPage = AysPageBuilder.from(mockRoles, mockAysPageable, mockFilter);

        // Then
        AysPage<AysRole> rolesPage = roleAdapter.findAll(mockAysPageable, mockFilter);

        AysPageBuilder.assertEquals(mockRolesPage, rolesPage);

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }


    @Test
    void givenValidId_whenRoleFound_thenReturnOptionalRole() {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        AysRoleEntity mockRoleEntity = new AysRoleEntityBuilder()
                .withValidValues()
                .withId(mockId)
                .build();
        Mockito.when(roleRepository.findById(mockId))
                .thenReturn(Optional.of(mockRoleEntity));

        // Then
        Optional<AysRole> role = roleAdapter.findById(mockId);

        Assertions.assertTrue(role.isPresent());
        Assertions.assertEquals(mockId, role.get().getId());

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findById(mockId);
    }

    @Test
    void givenValidId_whenRoleNotFound_thenReturnOptionalEmpty() {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(roleRepository.findById(mockId))
                .thenReturn(Optional.empty());

        // Then
        Optional<AysRole> role = roleAdapter.findById(mockId);

        Assertions.assertFalse(role.isPresent());

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findById(Mockito.anyString());
    }


    @Test
    void givenValidInstitutionId_whenActiveRolesFoundByInstitutionId_thenReturnRoles() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidValues()
                .withId(mockInstitutionId)
                .build();

        // When
        List<AysRoleEntity> mockRoleEntities = List.of(
                new AysRoleEntityBuilder()
                        .withValidValues()
                        .withInstitution(mockInstitutionEntity)
                        .build(),
                new AysRoleEntityBuilder()
                        .withValidValues()
                        .withInstitution(mockInstitutionEntity)
                        .build()
        );

        AysRoleStatus mockStatus = AysRoleStatus.ACTIVE;
        Mockito.when(roleRepository.findAllByInstitutionIdAndStatus(mockInstitutionId, mockStatus))
                .thenReturn(mockRoleEntities);

        // Then
        List<AysRole> roles = roleAdapter.findAllActivesByInstitutionId(mockInstitutionId);

        Assertions.assertTrue(CollectionUtils.isNotEmpty(roles));

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findAllByInstitutionIdAndStatus(mockInstitutionId, mockStatus);
    }

    @Test
    void givenValidInstitutionId_whenActiveRolesNotFoundByInstitutionId_thenReturnEmptySet() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        // When
        AysRoleStatus mockStatus = AysRoleStatus.ACTIVE;
        Mockito.when(roleRepository.findAllByInstitutionIdAndStatus(mockInstitutionId, mockStatus))
                .thenReturn(null);

        // Then
        List<AysRole> roles = roleAdapter.findAllActivesByInstitutionId(mockInstitutionId);

        Assertions.assertTrue(CollectionUtils.isEmpty(roles));

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findAllByInstitutionIdAndStatus(mockInstitutionId, mockStatus);
    }


    @Test
    void givenValidIds_whenAllRoleEntitiesFoundByIds_thenReturnListOfRoles() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        Set<String> mockIds = Set.of(mockId);

        // When
        List<AysRoleEntity> mockRoleEntities = List.of(
                new AysRoleEntityBuilder()
                        .withValidValues()
                        .withId(mockId)
                        .build()
        );
        Mockito.when(roleRepository.findAllById(mockIds))
                .thenReturn(mockRoleEntities);

        List<AysRole> mockRoles = roleEntityToDomainMapper.map(mockRoleEntities);

        // Then
        List<AysRole> roles = roleAdapter.findAllByIds(mockIds);

        Assertions.assertEquals(mockRoles, roles);

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findAllById(mockIds);
    }

    @Test
    void givenValidIds_whenAllRoleEntitiesNotFoundByIds_thenReturnEmptyList() {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        Set<String> mockIds = Set.of(mockId);

        // When
        List<AysRoleEntity> mockRoleEntities = List.of();
        Mockito.when(roleRepository.findAllById(mockIds))
                .thenReturn(mockRoleEntities);

        List<AysRole> mockRoles = roleEntityToDomainMapper.map(mockRoleEntities);

        // Then
        List<AysRole> roles = roleAdapter.findAllByIds(mockIds);

        Assertions.assertEquals(mockRoles, roles);

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findAllById(mockIds);
    }


    @Test
    void givenValidNameAndInstitutionId_whenRoleNotFound_thenReturnOptionalEmpty() {

        // Given
        String mockName = "Test Role";
        String mockInstitutionId = "4907d8a9-dd9b-47ff-9780-b1a6074e81d6";

        // When
        Mockito.when(roleRepository.findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Optional<AysRole> role = roleAdapter.findByNameAndInstitutionId(mockName, mockInstitutionId);

        Assertions.assertFalse(role.isPresent());

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByNameAndInstitutionId(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void givenValidNameAndInstitutionId_whenRoleFound_thenReturnOptionalRole() {

        // Given
        String mockName = "Test Role";
        String mockInstitutionId = "99921b54-5a19-4552-afb7-c3e8ffde3dd3";

        // When
        AysRoleEntity mockRoleEntity = new AysRoleEntityBuilder()
                .withValidValues()
                .withName(mockName)
                .build();
        Mockito.when(roleRepository.findByNameAndInstitutionId(mockName, mockInstitutionId))
                .thenReturn(Optional.of(mockRoleEntity));

        // Then
        Optional<AysRole> role = roleAdapter.findByNameAndInstitutionId(mockName, mockInstitutionId);

        Assertions.assertTrue(role.isPresent());
        Assertions.assertEquals(mockName, role.get().getName());

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByNameAndInstitutionId(mockName, mockInstitutionId);
    }


    @Test
    void givenValidRole_whenRoleSaved_thenReturnRoleFromDatabase() {

        // Given
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withoutId()
                .build();

        // When
        AysRoleEntity mockRoleEntity = roleToEntityMapper.map(mockRole);
        String mockId = AysRandomUtil.generateUUID();
        mockRoleEntity.setId(mockId);
        Mockito.when(roleRepository.save(Mockito.any(AysRoleEntity.class)))
                .thenReturn(mockRoleEntity);

        mockRole.setId(mockId);

        // Then
        AysRole role = roleAdapter.save(mockRole);

        Assertions.assertNotNull(role);
        Assertions.assertEquals(mockRole.getId(), role.getId());

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .save(Mockito.any(AysRoleEntity.class));
    }

}
