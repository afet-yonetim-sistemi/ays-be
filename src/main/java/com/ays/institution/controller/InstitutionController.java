package com.ays.institution.controller;

import com.ays.common.model.dto.response.AysResponse;
import com.ays.institution.model.Institution;
import com.ays.institution.model.dto.response.InstitutionResponse;
import com.ays.institution.model.dto.response.InstitutionsSummaryResponse;
import com.ays.institution.model.mapper.InstitutionToInstitutionSummaryResponseMapper;
import com.ays.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
class InstitutionController {

    private final InstitutionService institutionService;

    private final InstitutionToInstitutionSummaryResponseMapper institutionToInstitutionSummaryResponseMapper = InstitutionToInstitutionSummaryResponseMapper.initialize();

    /**
     * Retrieves a summary of all institutions.
     * Requires the user to have the 'SUPER_ADMIN' authority.
     *
     * @return An {@link AysResponse} containing a list of {@link InstitutionResponse} representing the summary of institutions.
     */
    @GetMapping("/institutions/summary")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public AysResponse<List<InstitutionsSummaryResponse>> getSummaryOfActiveInstitutions() {

        final List<Institution> institutionsSummary = institutionService.getSummaryOfActiveInstitutions();
        final List<InstitutionsSummaryResponse> institutionsSummaryRespons = institutionToInstitutionSummaryResponseMapper.map(institutionsSummary);
        return AysResponse.successOf(institutionsSummaryRespons);
    }

}
