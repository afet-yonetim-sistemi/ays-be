package org.ays.institution.controller;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.response.AysResponse;
import org.ays.institution.model.Institution;
import org.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import org.ays.institution.model.response.InstitutionsSummaryResponse;
import org.ays.institution.service.InstitutionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
