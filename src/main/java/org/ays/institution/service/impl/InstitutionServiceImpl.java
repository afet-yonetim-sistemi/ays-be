package org.ays.institution.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.ays.institution.model.Institution;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.request.InstitutionListRequest;
import org.ays.institution.port.InstitutionReadPort;
import org.ays.institution.service.InstitutionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link InstitutionService} interface for performing institution-related business operations.
 * Provides methods to fetch and manage {@link Institution} entities.
 */
@Service
@Slf4j
@RequiredArgsConstructor
class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionReadPort institutionReadPort;

    /**
     * Retrieves a pageable list of institutions based on the provided filtering and pagination criteria.
     * The method uses caching to improve performance and minimize database queries.
     *
     * @param listRequest the request object containing pagination and filtering information, including pageable details
     *                    and optional filters such as institution name and statuses.
     * @return a pageable object containing a list of {@link Institution} entities matching the specified criteria.
     */
    @Override
    @Cacheable(
            value = "institutionCache",
            key = "#listRequest.pageable.page + '-' + #listRequest.pageable.pageSize + '-' + #listRequest.filter?.name + '-' + #listRequest.filter?.statuses"
    )
    public AysPage<Institution> findAll(InstitutionListRequest listRequest) {
        log.info("Cache miss for Institution list: page={}, size={}, filter={}",
                listRequest.getPageable().getPage(),
                listRequest.getPageable().getPageSize(),
                listRequest.getFilter());

        final AysPageable aysPageable = listRequest.getPageable();

        return institutionReadPort.findAll(aysPageable, listRequest.getFilter());
    }

    /**
     * Retrieves a summary list of all active institutions, ordered by their names in ascending order.
     * The method is cached to improve performance and reduce a load on the database.
     *
     * @return a list of active {@link Institution} entities sorted by name in ascending order.
     */
    @Override
    @Cacheable(value = "institutions", key = "'summary'")
    public List<Institution> getSummaryOfActiveInstitutions() {
        log.info("Cache miss for Institution summary (ACTIVE). Loading from DB.");
        return institutionReadPort.findAllByStatusOrderByNameAsc(InstitutionStatus.ACTIVE);
    }

}
