package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.auth.model.mapper.AysRoleToEntityMapper;
import org.ays.auth.repository.AysRoleRepository;
import org.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

class AysRoleAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysRoleAdapter roleAdapter;

    @Mock
    private AysRoleRepository roleRepository;


    private final AysRoleToEntityMapper roleToEntityMapper = AysRoleToEntityMapper.initialize();


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
