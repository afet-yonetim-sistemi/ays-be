package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.mapper.AysRoleToEntityMapper;
import org.ays.auth.repository.AysRoleRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AysRoleAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysRoleAdapter roleAdapter;

    @Mock
    private AysRoleRepository roleRepository;


    private final AysRoleToEntityMapper roleToEntityMapper = AysRoleToEntityMapper.initialize();

}
