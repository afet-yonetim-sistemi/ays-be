package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.request.AysUserCreateRequest;
import org.ays.auth.service.AysUserCreateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class AysUserCreateServiceImpl implements AysUserCreateService {

    @Override
    public void create(AysUserCreateRequest createRequest) {
        // TODO: Implement the create method
    }

}
