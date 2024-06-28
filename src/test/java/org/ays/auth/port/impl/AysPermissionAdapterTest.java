package org.ays.auth.port.impl;

import org.ays.AysUnitTest;
import org.ays.auth.model.mapper.AysPermissionEntityToDomainMapper;
import org.ays.auth.repository.AysPermissionRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AysPermissionAdapterTest extends AysUnitTest {

    @InjectMocks
    private AysPermissionAdapter permissionAdapter;

    @Mock
    private AysPermissionRepository permissionRepository;


    private final AysPermissionEntityToDomainMapper permissionEntityToDomainMapper = AysPermissionEntityToDomainMapper.initialize();

}