package org.ays.auth.service;

import org.ays.auth.model.AysUser;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.common.model.AysPage;

public interface AysUserReadService {

    //todo javadoc
    AysPage<AysUser> findAllByInstitutionId(AysUserListRequest listRequest);
}
