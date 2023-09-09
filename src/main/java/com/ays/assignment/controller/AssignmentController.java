package com.ays.assignment.controller;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.dto.request.AssignmentSearchRequest;
import com.ays.assignment.model.dto.response.AssignmentResponse;
import com.ays.assignment.model.dto.response.AssignmentSearchResponse;
import com.ays.assignment.model.mapper.AssignmentToAssignmentResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSearchResponseMapper;
import com.ays.assignment.service.AssignmentActionService;
import com.ays.assignment.service.AssignmentSaveService;
import com.ays.assignment.service.AssignmentSearchService;
import com.ays.assignment.service.AssignmentService;
import com.ays.common.model.dto.response.AysResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller class for managing assignment-related operations via HTTP requests.
 * This controller handles the CRUD operations for assignments in the system.
 * The mapping path for this controller is "/api/v1/assignment".
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
class AssignmentController {

    private final AssignmentSaveService assignmentSaveService;
    private final AssignmentSearchService assignmentSearchService;
    private final AssignmentService assignmentService;
    private final AssignmentActionService assignmentActionService;

    private static final AssignmentToAssignmentResponseMapper assignmentToAssignmentResponseMapper = AssignmentToAssignmentResponseMapper.initialize();
    private static final AssignmentToAssignmentSearchResponseMapper assignmentToAssignmentSearchResponseMapper = AssignmentToAssignmentSearchResponseMapper.initialize();

    /**
     * Saves a new assignment to the system.
     * Requires ADMIN authority.
     *
     * @param saveRequest The request object containing the assignment data to be saved.
     * @return A response object containing the saved assignment data.
     */
    @PostMapping("/assignment")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<Void> saveAssignment(@RequestBody @Valid AssignmentSaveRequest saveRequest) {
        assignmentSaveService.saveAssignment(saveRequest);
        return AysResponse.SUCCESS;
    }

    /**
     * Gets a user by ID.
     * Requires ADMIN authority.
     *
     * @param id The ID of the user to retrieve.
     * @return A response object containing the retrieved user data.
     */
    @GetMapping("/assignment/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<AssignmentResponse> getAssignmentById(@PathVariable @UUID String id) {

        final Assignment assignment = assignmentService.getAssignmentById(id);
        final AssignmentResponse assignmentResponse = assignmentToAssignmentResponseMapper.map(assignment);
        return AysResponse.successOf(assignmentResponse);
    }

    /**
     * Retrieves nearest assignment by AssignmentSearchRequest.
     * Requires USER authority.
     *
     * @param searchRequest The request object containing user location to search nearest assignment.
     * @return A response object containing nearest assignment data.
     */
    @PostMapping("/assignment/search")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<AssignmentSearchResponse> searchUserAssignment(@RequestBody @Valid AssignmentSearchRequest searchRequest) {
        final Assignment assignment = assignmentSearchService.searchAssignment(searchRequest);
        final AssignmentSearchResponse assignmentResponse = assignmentToAssignmentSearchResponseMapper.map(assignment);
        return AysResponse.successOf(assignmentResponse);
    }

    // approve assignment and requires user authority

    /**
     * Approves assignment by id.
     * Requires USER authority.
     *
     * @return A success response if assignment approved.
     */
    @PostMapping("/assignment/approve")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<Void> approveAssignment() {
        assignmentActionService.approveAssignment();
        return AysResponse.SUCCESS;
    }
}
