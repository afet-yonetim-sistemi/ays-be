package org.ays.institution.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.institution.model.Institution;
import org.ays.institution.model.enums.InstitutionStatus;
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
     * Retrieves a summary of all active {@link Institution} entities, ordered by name in ascending order.
     *
     * @return a list of {@link Institution} representing the summary of active institutions
     */
    @Override
    public List<Institution> getSummaryOfActiveInstitutions() {
        return institutionReadPort.findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE);
    }

}
