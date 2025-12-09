package org.ays.institution.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.ays.institution.model.Institution;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.request.InstitutionListRequest;
import org.ays.institution.port.InstitutionReadPort;
import org.ays.institution.service.InstitutionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link InstitutionService} interface for performing institution-related business operations.
 * Provides methods to fetch and manage {@link Institution} entities.
 */
@Service
@RequiredArgsConstructor
class InstitutionServiceImpl implements InstitutionService {

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

    /**
     * Retrieves a summary of all active {@link Institution} entities, ordered by name in ascending order.
     *
     * @return a list of {@link Institution} representing the summary of active institutions
     */
    @Override
    public List<Institution> getSummaryOfActiveInstitutions() {
        return institutionReadPort.findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE);
    }

}
