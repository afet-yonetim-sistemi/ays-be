package org.ays.institution.service;

import org.ays.institution.model.Institution;

import java.util.List;

/**
 * Institution Service to perform institution related business operations.
 */
public interface InstitutionService {

    /**
     * Get all active institutions summary
     *
     * @return list of active institutions
     */
    List<Institution> getSummaryOfActiveInstitutions();

}
