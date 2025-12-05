package org.ays.institution.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.institution.model.Institution;
import org.ays.institution.model.mapper.InstitutionToInstitutionsResponseMapper;
import org.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import org.ays.institution.model.request.InstitutionListRequest;
import org.ays.institution.model.response.InstitutionsResponse;
import org.ays.institution.model.response.InstitutionsSummaryResponse;
import org.ays.institution.service.InstitutionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller class for managing institution-related operations via HTTP requests.
 * This controller handles the business operations for institutions in the system.
 */
@RestController
@RequiredArgsConstructor
class InstitutionController {

    private final InstitutionService institutionService;

    private final InstitutionToInstitutionsSummaryResponseMapper institutionToInstitutionsSummaryResponseMapper = InstitutionToInstitutionsSummaryResponseMapper.initialize();
    private final InstitutionToInstitutionsResponseMapper institutionToInstitutionsResponseMapper = InstitutionToInstitutionsResponseMapper.initialize();

    /**
     * Retrieve all institutions based on the provided filtering and pagination criteria.
     * <p>
     * This endpoint handles the retrieval of institutions based on the filtering and pagination criteria
     * provided in {@link InstitutionListRequest}. The user must have a authority to access this endpoint.
     * </p>
     *
     * @param request the request object containing filtering and pagination criteria.
     * @return an {@link AysResponse} indicating the success of the operation.
     */
    @PostMapping("/api/institution/v1/institutions")
    @PreAuthorize("hasAnyAuthority('institution:list')")
    public AysResponse<AysPageResponse<InstitutionsResponse>> findAll(@RequestBody @Valid InstitutionListRequest request) {

        AysPage<Institution> pageOfInstitutions = institutionService.findAll(request);

        final AysPageResponse<InstitutionsResponse> pageOfInstitutionsResponse = AysPageResponse.<InstitutionsResponse>builder()
                .of(pageOfInstitutions)
                .content(institutionToInstitutionsResponseMapper.map(pageOfInstitutions.getContent()))
                .build();

        return AysResponse.successOf(pageOfInstitutionsResponse);
    }

    /**
     * Retrieves a summary of all institutions.
     * Requires the user to have the 'application:registration:create' authority.
     *
     * @return An {@link AysResponse} containing a list of {@link InstitutionsSummaryResponse} representing the summary of institutions.
     */
    @GetMapping("/api/institution/v1/institutions/summary")
    @PreAuthorize("hasAnyAuthority('application:registration:create')")
    public AysResponse<List<InstitutionsSummaryResponse>> getSummaryOfActiveInstitutionsForInstitution() {

        final List<Institution> institutionsSummary = institutionService.getSummaryOfActiveInstitutions();
        final List<InstitutionsSummaryResponse> institutionsSummaryResponse = institutionToInstitutionsSummaryResponseMapper.map(institutionsSummary);
        return AysResponse.successOf(institutionsSummaryResponse);
    }

    /**
     * Retrieves a summary of all institutions.
     *
     * @return An {@link AysResponse} containing a list of {@link InstitutionsSummaryResponse} representing the summary of institutions.
     */
    @GetMapping("/api/landing/v1/institutions/summary")
    public AysResponse<List<InstitutionsSummaryResponse>> getSummaryOfActiveInstitutionsForLanding() {

        final List<Institution> institutionsSummary = institutionService.getSummaryOfActiveInstitutions();
        final List<InstitutionsSummaryResponse> institutionsSummaryResponses = institutionToInstitutionsSummaryResponseMapper.map(institutionsSummary);
        return AysResponse.successOf(institutionsSummaryResponses);
    }

}
