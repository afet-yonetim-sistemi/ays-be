package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.entity.AysRoleEntityBuilder;
import org.ays.auth.model.mapper.AysRoleToEntityMapper;
import org.ays.auth.repository.AysRoleRepository;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class AysRoleAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysRoleAdapter roleAdapter;

    @Mock
    private AysRoleRepository roleRepository;


    private final AysRoleToEntityMapper roleToEntityMapper = AysRoleToEntityMapper.initialize();


    @Test
    void givenValidName_whenRoleNotFoundByName_thenReturnOptionalEmpty() {

        // Given
        String mockName = "Test Role";

        // When
        Mockito.when(roleRepository.findByName(mockName))
                .thenReturn(Optional.empty());

        // Then
        Optional<AysRole> role = roleAdapter.findByName(mockName);

        Assertions.assertFalse(role.isPresent());

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByName(mockName);
    }

    @Test
    void givenValidName_whenRoleFoundByName_thenReturnOptionalRole() {

        // Given
        String mockName = "Test Role";

        // When
        AysRoleEntity mockRoleEntity = new AysRoleEntityBuilder()
                .withValidValues()
                .withName(mockName)
                .build();
        Mockito.when(roleRepository.findByName(mockName))
                .thenReturn(Optional.of(mockRoleEntity));

        // Then
        Optional<AysRole> role = roleAdapter.findByName(mockName);

        Assertions.assertTrue(role.isPresent());
        Assertions.assertEquals(mockName, role.get().getName());

        // Verify
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByName(mockName);
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
