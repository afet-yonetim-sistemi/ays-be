package org.ays.institution.controller;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.response.AysResponse;
import org.ays.institution.model.Institution;
import org.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import org.ays.institution.model.response.InstitutionResponse;
import org.ays.institution.model.response.InstitutionsSummaryResponse;
import org.ays.institution.service.InstitutionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller class for managing institution-related operations via HTTP requests.
 * This controller handles the business operations for institutions in the system.
 * The mapping path for this controller is "/api/v1/institutions".
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class InstitutionController {

    private final InstitutionService institutionService;


    private final InstitutionToInstitutionsSummaryResponseMapper institutionToInstitutionsSummaryResponseMapper = InstitutionToInstitutionsSummaryResponseMapper.initialize();


    /**
     * Retrieves a summary of all institutions.
     * Requires the user to have the 'application:registration:create' authority.
     *
     * @return An {@link AysResponse} containing a list of {@link InstitutionResponse} representing the summary of institutions.
     */
    @GetMapping("/institutions/summary")
    @PreAuthorize("hasAnyAuthority('application:registration:create')")
    public AysResponse<List<InstitutionsSummaryResponse>> getSummaryOfActiveInstitutions() {

        final List<Institution> institutionsSummary = institutionService.getSummaryOfActiveInstitutions();
        final List<InstitutionsSummaryResponse> institutionsSummaryRespons = institutionToInstitutionsSummaryResponseMapper.map(institutionsSummary);
        return AysResponse.successOf(institutionsSummaryRespons);
    }

}
