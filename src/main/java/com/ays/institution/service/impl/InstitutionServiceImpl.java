package com.ays.institution.service.impl;

import com.ays.institution.model.Institution;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.enums.InstitutionStatus;
import com.ays.institution.model.mapper.InstitutionEntityToInstitutionMapper;
import com.ays.institution.repository.InstitutionRepository;
import com.ays.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link InstitutionService} interface for performing institution-related business operations.
 */
@Service
@RequiredArgsConstructor
class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository institutionRepository;

    private final InstitutionEntityToInstitutionMapper institutionEntityToInstitutionMapper = InstitutionEntityToInstitutionMapper.initialize();

    /**
     * Retrieves a summary of all active institutions.
     *
     * @return a list of {@link Institution} representing the summary of institutions
     */
    @Override
    public List<Institution> getSummaryOfActiveInstitutions() {
        final List<InstitutionEntity> activeInstitutions = institutionRepository.findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE);
        return institutionEntityToInstitutionMapper.map(activeInstitutions);
    }

}
