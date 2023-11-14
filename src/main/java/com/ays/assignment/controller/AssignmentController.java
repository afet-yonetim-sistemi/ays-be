package com.ays.assignment.controller;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.*;
import com.ays.assignment.model.dto.response.*;
import com.ays.assignment.model.mapper.*;
import com.ays.assignment.service.AssignmentConcludeService;
import com.ays.assignment.service.AssignmentSaveService;
import com.ays.assignment.service.AssignmentSearchService;
import com.ays.assignment.service.AssignmentService;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
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

    private final AssignmentConcludeService assignmentConcludeService;

    private final AssignmentToAssignmentResponseMapper assignmentToAssignmentResponseMapper = AssignmentToAssignmentResponseMapper.initialize();
    private final AssignmentToAssignmentSearchResponseMapper assignmentToAssignmentSearchResponseMapper = AssignmentToAssignmentSearchResponseMapper.initialize();
    private final AssignmentToAssignmentsResponseMapper assignmentToAssignmentsResponseMapper = AssignmentToAssignmentsResponseMapper.initialize();
    private final AssignmentToAssignmentSummaryResponseMapper assignmentToAssignmentSummaryResponseMapper = AssignmentToAssignmentSummaryResponseMapper.initialize();
    private final AssignmentToAssignmentUserResponseMapper assignmentToAssignmentUserResponseMapper = AssignmentToAssignmentUserResponseMapper.initialize();

    /**
     * Gets an Assignments list based on the specified filters in the {@link AssignmentListRequest}
     * Requires ADMIN authority
     *
     * @param listRequest The assignment request that contains the status filter
     * @return A response object that contains the retrieved assignments' data
     * @see AssignmentListRequest
     */
    @PostMapping("/assignments")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<AysPageResponse<AssignmentsResponse>> getAssignments(@RequestBody @Valid AssignmentListRequest listRequest) {
        final AysPage<Assignment> pageOfAssignments = assignmentService.getAssignments(listRequest);
        final AysPageResponse<AssignmentsResponse> pageOfAssignmentsResponse = AysPageResponse
                .<AssignmentsResponse>builder()
                .of(pageOfAssignments)
                .content(assignmentToAssignmentsResponseMapper.map(pageOfAssignments.getContent()))
                .filteredBy(listRequest.getFilter())
                .build();
        return AysResponse.successOf(pageOfAssignmentsResponse);
    }

    /**
     * Gets the assignment that assigns to the current user.
     * Requires USER authority.
     *
     * @return A response object containing the retrieved user's assignment data.
     */
    @GetMapping("/assignment")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<AssignmentUserResponse> getUserAssignment() {
        final Assignment assignment = assignmentService.getUserAssignment();
        final AssignmentUserResponse assignmentUserResponse = assignmentToAssignmentUserResponseMapper.map(assignment);
        return AysResponse.successOf(assignmentUserResponse);
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
     * Gets a summary of the assignments.
     * Requires USER authority.
     *
     * @return A response object containing the assignment summary data.
     */
    @GetMapping("/assignment/summary")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<AssignmentSummaryResponse> getAssignmentSummary() {

        final Assignment assignment = assignmentService.getAssignmentSummary();
        final AssignmentSummaryResponse assignmentSummaryResponse = assignmentToAssignmentSummaryResponseMapper
                .map(assignment);
        return AysResponse.successOf(assignmentSummaryResponse);
    }

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

    /**
     * Updates an assignment with the provided ID and request.
     *
     * @param id            The ID of the assignment to be updated.
     * @param updateRequest The request containing the new assignment information.
     * @return A success response indicating that the assignment has been updated.
     */
    @PutMapping("/assignment/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<Void> updateAssignment(@PathVariable @UUID String id,
                                              @RequestBody @Valid AssignmentUpdateRequest updateRequest) {
        assignmentService.updateAssignment(id, updateRequest);
        return AysResponse.SUCCESS;
    }

    /**
     * Deletes an assignment with the specified ID.
     * This method is accessible via a DELETE request to "/assignment/{id}" endpoint,
     * and requires the caller to have the 'ADMIN' authority.
     *
     * @param id The unique identifier of the assignment to be deleted.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping("/assignment/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<Void> deleteAssignment(@PathVariable @UUID String id) {
        assignmentService.deleteAssignment(id);
        return AysResponse.SUCCESS;
    }

    /**
     * Approves an assignment that is reserved by user
     * Requires USER authority.
     *
     * @return A success response if assignment approved.
     */
    @PostMapping("/assignment/approve")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<Void> approveAssignment() {
        assignmentConcludeService.approve();
        return AysResponse.SUCCESS;
    }

    /**
     * Rejects an assignment that is reserved by user
     * Requires USER authority.
     *
     * @return A success response if assignment rejected.
     */
    @PostMapping("/assignment/reject")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<Void> rejectAssignment() {
        assignmentConcludeService.reject();
        return AysResponse.SUCCESS;
    }

    /**
     * Starts assignment that is approved by user.
     * Requires USER authority.
     *
     * @return A success response if assignment started.
     */
    @PostMapping("/assignment/start")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<Void> startAssignment() {
        assignmentConcludeService.start();
        return AysResponse.SUCCESS;
    }

    /**
     * Completes assignment that is started by user.
     * Requires USER authority.
     *
     * @return A success response if assignment completed.
     */
    @PostMapping("/assignment/complete")
    @PreAuthorize("hasAnyAuthority('USER')")
    public AysResponse<Void> completeAssignment() {
        assignmentConcludeService.complete();
        return AysResponse.SUCCESS;
    }

    /**
     * Cancels assignment that is either started by user or assigned to user.
     * Requires USER authority.
     *
     * @param cancelRequest The request object containing cancel reason.
     * @return A success response if assignment canceled.
     */
    @PostMapping("/assignment/cancel")
    @PreAuthorize("hasAnyAuthority('USER')")
    @SuppressWarnings("unused")
    public AysResponse<Void> cancelAssignment(@Valid AssignmentCancelRequest cancelRequest) {
        assignmentConcludeService.cancel();
        return AysResponse.SUCCESS;
    }

}
