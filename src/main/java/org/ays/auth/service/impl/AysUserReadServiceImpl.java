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

/**
 * Service implementation for reading users.
 * <p>
 * The {@code AysUserReadServiceImpl} class provides the logic for retrieving users based on the provided request
 * and ensures that the users retrieved are scoped to the institution of the current user.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AysUserReadServiceImpl implements AysUserReadService {

    private final AysUserReadPort userReadPort;
    private final AysIdentity identity;


    /**
     * Retrieves a paginated list of users based on the provided request.
     * <p>
     * If no filter is provided in the request, a default filter is applied which scopes the results
     * to the institution associated with the current user.
     * </p>
     *
     * @param listRequest the request containing pagination and filtering information.
     * @return a paginated list of users.
     */
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
