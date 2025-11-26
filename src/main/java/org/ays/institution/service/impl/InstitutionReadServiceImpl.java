package org.ays.institution.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionFilter;
import org.ays.institution.model.request.InstitutionListRequest;
import org.ays.institution.port.InstitutionReadPort;
import org.ays.institution.service.InstitutionReadService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for reading institutions.
 * <p>
 * The {@code InstitutionReadServiceImpl} class provides the logic for retrieving institutions based on the provided request.
 * </p>
 */
@Service
@RequiredArgsConstructor
class InstitutionReadServiceImpl implements InstitutionReadService {

    private final InstitutionReadPort institutionReadPort;


    /**
     * Retrieves a paginated list of institutions based on the provided request.
     *
     * @param listRequest the request containing pagination and filtering information.
     * @return a paginated list of institutions.
     */
    @Override
    public AysPage<Institution> findAll(InstitutionListRequest listRequest) {

        final AysPageable aysPageable = listRequest.getPageable();

        return institutionReadPort.findAll(aysPageable, listRequest.getFilter());
    }

}
