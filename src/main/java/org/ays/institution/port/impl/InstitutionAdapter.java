package org.ays.institution.port.impl;

import lombok.RequiredArgsConstructor;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.mapper.InstitutionEntityToInstitutionMapper;
import org.ays.institution.port.InstitutionReadPort;
import org.ays.institution.repository.InstitutionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * An adapter class implementing {@link InstitutionReadPort} for accessing {@link Institution} entities.
 * This class interacts with the underlying repository to fetch the data and uses a mapper
 * to convert entity objects to domain objects.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
class InstitutionAdapter implements InstitutionReadPort {

    private final InstitutionRepository institutionRepository;


    private final InstitutionEntityToInstitutionMapper institutionEntityToInstitutionMapper = InstitutionEntityToInstitutionMapper.initialize();

    /**
     * Retrieves a list of all institutions based on the given status, ordered by name in ascending order.
     *
     * @param status the status of the institutions to retrieve
     * @return a list of {@link Institution} entities representing the institutions with the specified status
     */
    @Override
    public List<Institution> findAllByStatusOrderByNameAsc(final InstitutionStatus status) {
        final List<InstitutionEntity> activeInstitutions = institutionRepository.findAllByStatusOrderByNameAsc(status);
        return institutionEntityToInstitutionMapper.map(activeInstitutions);
    }

    @Override
    public boolean existsByIdAndIsStatusActive(final String id) {
        return institutionRepository.existsByIdAndStatus(id, InstitutionStatus.ACTIVE);
    }

}
