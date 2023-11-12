package com.ays.institution.service;

import com.ays.institution.model.Institution;

import java.util.List;

/**
 * Institution Service to perform institution related business operations.
 */
public interface InstitutionService {

    /**
     * Get all institutions summary
     *
     * @return list of InstitutionResponse
     */
    List<Institution> getInstitutionsSummary();

}
