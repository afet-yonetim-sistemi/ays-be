package com.ays.institution.service;

import com.ays.common.model.AysPage;
import com.ays.institution.model.dto.response.InstitutionResponse;

/**
 * Institution Service to perform institution related business operations.
 */
public interface InstitutionService {

    /**
     * Get all institutions summary
     * @return AysPage of InstitutionResponse
     */
    AysPage<InstitutionResponse> getInstitutionsSummary();

}
