package org.ays.institution.service;

import org.ays.common.model.AysPage;
import org.ays.institution.model.Institution;
import org.ays.institution.model.request.InstitutionListRequest;

/**
 * Service interface for reading and retrieving institutions in a paginated format.
 * <p>
 * The {@link InstitutionReadService} provides a method for retrieving a paginated list of institutions
 * based on the specified request parameters. Implementations of this interface should handle
 * the retrieval of institutions, including any necessary filtering, sorting, and pagination logic
 * as defined in the {@link InstitutionListRequest}.
 * </p>
 *
 * @see Institution
 * @see InstitutionListRequest
 * @see AysPage
 */
public interface InstitutionReadService {

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

}
