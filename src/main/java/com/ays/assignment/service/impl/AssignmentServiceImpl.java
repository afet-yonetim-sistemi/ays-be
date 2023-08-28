package com.ays.assignment.service.impl;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentListRequest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.AssignmentService;
import com.ays.assignment.util.exception.AysAssignmentNotExistByIdException;
import com.ays.auth.model.AysIdentity;
import com.ays.common.model.AysPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    private final AysIdentity identity;

    private static final AssignmentEntityToAssignmentMapper assignmentEntityToAssignmentMapper = AssignmentEntityToAssignmentMapper.initialize();

    /**
     * Retrieves an assignment by their ID.
     *
     * @param id the ID of the assignment to retrieve
     * @return the Assignment object representing the retrieved user
     * @throws AysAssignmentNotExistByIdException if the assignment with the specified ID does not exist
     */
    @Override
    public Assignment getAssignmentById(String id) {

        final AssignmentEntity assignmentEntity = assignmentRepository.findByIdAndInstitutionId(id, identity.getInstitutionId())
                .orElseThrow(() -> new AysAssignmentNotExistByIdException(id));

        return assignmentEntityToAssignmentMapper.map(assignmentEntity);
    }

    /**
     * Retrieves a paginated list of assignments based on the specified filters and the institution's identity.
     *
     * @param listRequest The request containing filters and pagination parameters.
     * @return An {@link AysPage} object containing the filtered assignments.
     */
    @Override
    public AysPage<Assignment> getAssignments(AssignmentListRequest listRequest) {

        Specification<AssignmentEntity> byStatus = listRequest.toSpecification(AssignmentEntity.class);
        Specification<AssignmentEntity> byInstitutionId = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("institutionId"),identity.getInstitutionId());

        Page<AssignmentEntity> assignmentEntities = assignmentRepository
                .findAll(byStatus.and(byInstitutionId),listRequest.toPageable());

        List<Assignment> assignments = assignmentEntityToAssignmentMapper.map(assignmentEntities.getContent());

        return AysPage.of(
                listRequest.getFilter(),
                assignmentEntities,
                assignments
        );
    }
}
