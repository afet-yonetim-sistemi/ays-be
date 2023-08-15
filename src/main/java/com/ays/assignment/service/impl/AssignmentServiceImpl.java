package com.ays.assignment.service.impl;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentListRequest;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.AssignmentService;
import com.ays.auth.model.AysIdentity;
import com.ays.common.model.AysPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AssignmentService interface.
 * This class provides methods to perform assignment-related operations such as retrieving assignments, updating assignment information, and deleting assignment.
 */
@Service
@RequiredArgsConstructor
class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    private final AysIdentity identity;

    @Override
    public AysPage<Assignment> getAllAssignments(AssignmentListRequest listRequest) {
        return null;
    }

    @Override
    public Assignment getAssignmentById(String id) {
        return null;
    }

    @Override
    public void updateAssignment(String id, AssignmentUpdateRequest updateRequest) {

    }

    @Override
    public void deleteAssignment(String id) {

    }
}
