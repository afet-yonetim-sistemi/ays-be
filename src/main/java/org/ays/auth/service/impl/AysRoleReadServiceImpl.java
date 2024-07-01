package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleFilter;
import org.ays.auth.model.request.AysRoleListRequest;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.service.AysRoleReadService;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service implementation for reading roles.
 * <p>
 * The {@code AysRoleReadServiceImpl} class provides the logic for retrieving roles based on the provided request
 * and ensures that the roles retrieved are scoped to the institution of the current user.
 * </p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class AysRoleReadServiceImpl implements AysRoleReadService {

    private final AysRoleReadPort roleReadPort;

    private final AysIdentity identity;


    /**
     * Retrieves all roles.
     * <p>
     * This method retrieves all roles from the data source and returns them as a set.
     * </p>
     *
     * @return a set of {@link AysRole} objects representing all roles.
     */
    @Override
    public Set<AysRole> findAll() {
        return new HashSet<>(roleReadPort.findAll());
    }


    /**
     * Retrieves a paginated list of roles based on the provided request.
     * <p>
     * If no filter is provided in the request, a default filter is applied which scopes the results
     * to the institution associated with the current user.
     * </p>
     *
     * @param listRequest the request containing pagination and filtering information.
     * @return a paginated list of roles.
     */
    @Override
    public AysPage<AysRole> findAll(final AysRoleListRequest listRequest) {

        final AysPageable aysPageable = listRequest.getPageable();

        Optional.ofNullable(listRequest.getFilter())
                .ifPresentOrElse(filter -> {
                            if (filter.getInstitutionId() == null) {
                                filter.setInstitutionId(identity.getInstitutionId());
                            }
                        },
                        () -> {
                            AysRoleFilter filter = AysRoleFilter.builder()
                                    .institutionId(identity.getInstitutionId())
                                    .build();

                            listRequest.setFilter(filter);
                        });

        return roleReadPort.findAll(aysPageable, listRequest.getFilter());
    }

}
