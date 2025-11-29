package org.ays.institution.service;

import org.ays.common.model.AysPage;
import org.ays.institution.model.Institution;
import org.ays.institution.model.request.InstitutionListRequest;

import java.util.List;

/**
 * Institution Service to perform institution related business operations.
 */
public interface InstitutionService {

    /**
     * Retrieves a paginated list of institutions based on the specified {@link InstitutionListRequest}.
     * <p>
     * This method handles the retrieval of institutions from a data source according to the parameters
     * defined in the {@code listRequest}. It typically involves filtering, sorting, and paginating
     * the results to match the request criteria.
     * </p>
     *
     * @param listRequest the request containing parameters for filtering, sorting, and pagination.
     * @return a paginated list of institutions matching the request criteria.
     */
    AysPage<Institution> findAll(InstitutionListRequest listRequest);

    /**
     * Get all active institutions summary
     *
     * @return list of active institutions
     */
    List<Institution> getSummaryOfActiveInstitutions();

}
