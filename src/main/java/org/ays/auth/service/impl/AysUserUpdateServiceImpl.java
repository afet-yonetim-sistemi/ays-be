package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.mapper.AysUserUpdateRequestToDomainMapper;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysUserUpdateService;
import org.ays.auth.util.exception.AysUserNotExistByIdException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AysUserUpdateServiceImpl implements AysUserUpdateService {

    private final AysUserReadPort userReadPort;
    private final AysUserSavePort userSavePort;


    private final AysUserUpdateRequestToDomainMapper requestToDomainMapper = AysUserUpdateRequestToDomainMapper.initialize();


    @Override
    public void update(final String id,
                       final AysUserUpdateRequest updateRequest) {

        final AysUser user = userReadPort.findById(id)
                .orElseThrow(() -> new AysUserNotExistByIdException(id));

        //user = requestToDomainMapper.map(updateRequest);
        userSavePort.save(user);
    }

}
