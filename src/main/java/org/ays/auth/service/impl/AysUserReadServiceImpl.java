package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.service.AysUserReadService;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AysUserReadServiceImpl implements AysUserReadService {

    //todo javadoc and cover with test

    private final AysUserReadPort aysUserReadPort;

    @Override
    public AysPage<AysUser> findAllByInstitutionId(AysUserListRequest listRequest) {

        final AysPageable aysPageable = listRequest.getPageable();

        return aysUserReadPort.findAll(aysPageable, listRequest.getFilter(), listRequest.getInstitutionId());
    }


}
