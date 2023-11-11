package com.ays.institution.service.impl;

import com.ays.common.model.AysPage;
import com.ays.institution.model.dto.response.InstitutionResponse;
import com.ays.institution.repository.InstitutionRepository;
import com.ays.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository institutionRepository;

    @Override
    public AysPage<InstitutionResponse> getInstitutionsSummary() {
        return null;
    }
}
