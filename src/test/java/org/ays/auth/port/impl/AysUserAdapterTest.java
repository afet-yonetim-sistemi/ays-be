package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.mapper.AysUserEntityToDomainMapper;
import org.ays.auth.model.mapper.AysUserToEntityMapper;
import org.ays.auth.repository.AysUserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AysUserAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysUserAdapter userAdapter;

    @Mock
    private AysUserRepository userRepository;


    private final AysUserToEntityMapper userToEntityMapper = AysUserToEntityMapper.initialize();
    private final AysUserEntityToDomainMapper userEntityToDomainMapper = AysUserEntityToDomainMapper.initialize();

}
