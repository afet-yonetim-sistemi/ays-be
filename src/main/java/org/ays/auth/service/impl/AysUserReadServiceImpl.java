package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserFilter;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.service.AysUserReadService;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AysUserReadServiceImpl implements AysUserReadService {

    //todo javadoc and cover with test

    private final AysUserReadPort userReadPort;
    private final AysIdentity identity;

    @Override
    public AysPage<AysUser> findAll(AysUserListRequest listRequest) {

        final AysPageable aysPageable = listRequest.getPageable();

        Optional.ofNullable(listRequest.getFilter())
                .ifPresentOrElse(
                        filter -> {
                            if (filter.getInstitutionId() == null) {
                                filter.setInstitutionId(identity.getInstitutionId());
                            }
                        },
                        () -> {
                            AysUserFilter filter = AysUserFilter.builder()
                                    .institutionId(identity.getInstitutionId())
                                    .build();

                            listRequest.setFilter(filter);
                        }
                );

        return userReadPort.findAll(aysPageable, listRequest.getFilter());
    }

}
