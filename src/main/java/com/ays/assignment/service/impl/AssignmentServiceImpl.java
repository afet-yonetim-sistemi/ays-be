package com.ays.assignment.service.impl;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentListRequest;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.AssignmentService;
import com.ays.assignment.util.exception.AysAssignmentAlreadyDeletedException;
import com.ays.assignment.util.exception.AysAssignmentNotExistByIdException;
import com.ays.auth.model.AysIdentity;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysSpecification;
import com.ays.user.util.exception.AysUserNotExistByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the AssignmentService interface.
 * This class provides methods to perform assignment-related operations such as retrieving assignments, updating assignment information, and deleting assignment.
 */
@Service
@RequiredArgsConstructor
class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    private final AysIdentity identity;

    private static final AssignmentEntityToAssignmentMapper assignmentEntityToAssignmentMapper = AssignmentEntityToAssignmentMapper.initialize();

    @Override
    public AysPage<Assignment> getAllAssignments(AssignmentListRequest listRequest) {

        final Map<String, Object> filter = Map.of("institutionId", identity.getInstitutionId());
        final Specification<AssignmentEntity> specification = AysSpecification.<AssignmentEntity>builder().and(filter);

        Page<AssignmentEntity> assignmentEntities = assignmentRepository.findAll(specification, listRequest.toPageable());
        List<Assignment> assignments = assignmentEntityToAssignmentMapper.map(assignmentEntities.getContent());
        return AysPage.of(
                assignmentEntities,
                assignments
        );

    }

    @Override
    public Assignment getAssignmentById(String id) {

        final AssignmentEntity assignmentEntity = assignmentRepository.findByIdAndInstitutionId(id, identity.getInstitutionId())
                .orElseThrow(() -> new AysAssignmentNotExistByIdException(id));

        return assignmentEntityToAssignmentMapper.map(assignmentEntity);

    }

    @Override
    public void updateAssignment(String id, AssignmentUpdateRequest updateRequest) {

        final AssignmentEntity assignmentEntity = assignmentRepository.findByIdAndInstitutionId(id, identity.getInstitutionId())
                .filter(assignment -> !assignment.isDeleted())
                .orElseThrow(() -> new AysUserNotExistByIdException(id));


        assignmentEntity.updateAssignmentStatus(updateRequest.getStatus());
        assignmentRepository.save(assignmentEntity);
    }

    @Override
    public void deleteAssignment(String id) {

        final AssignmentEntity assignmentEntity = assignmentRepository.findByIdAndInstitutionId(id, identity.getInstitutionId())
                .orElseThrow(() -> new AysAssignmentNotExistByIdException(id));

        if (assignmentEntity.isDeleted()) {
            throw new AysAssignmentAlreadyDeletedException(id);
        }

        assignmentEntity.deleteUser();
        assignmentRepository.save(assignmentEntity);
    }
}
