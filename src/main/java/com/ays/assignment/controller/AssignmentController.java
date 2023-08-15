package com.ays.assignment.controller;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.dto.response.AssignmentSavedResponse;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSavedResponseMapper;
import com.ays.assignment.service.AssignmentSaveService;
import com.ays.assignment.service.AssignmentService;
import com.ays.common.model.dto.response.AysResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final AssignmentService assignmentService;
    private final AssignmentSaveService assignmentSaveService;

    private final AssignmentToAssignmentSavedResponseMapper assignmentToAssignmentSavedResponseMapper = AssignmentToAssignmentSavedResponseMapper.initialize();

    /**
     * Saves a new assignment to the system.
     * Requires ADMIN authority.
     *
     * @param saveRequest The request object containing the assignment data to be saved.
     * @return A response object containing the saved assignment data.
     */
    @PostMapping("/assignment")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AysResponse<AssignmentSavedResponse> saveAssignment(@RequestBody @Valid AssignmentSaveRequest saveRequest) {
        Assignment assignment = assignmentSaveService.saveAssignment(saveRequest);
        AssignmentSavedResponse assignmentSavedResponse = assignmentToAssignmentSavedResponseMapper.map(assignment);
        return AysResponse.successOf(assignmentSavedResponse);
    }
}
