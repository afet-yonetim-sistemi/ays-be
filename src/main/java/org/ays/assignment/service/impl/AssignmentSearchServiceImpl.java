package org.ays.assignment.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.assignment.model.Assignment;
import org.ays.assignment.model.dto.request.AssignmentSearchRequest;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import org.ays.assignment.repository.AssignmentRepository;
import org.ays.assignment.service.AssignmentSearchService;
import org.ays.assignment.util.exception.AysAssignmentNotExistByPointException;
import org.ays.assignment.util.exception.AysAssignmentUserAlreadyAssigned;
import org.ays.assignment.util.exception.AysAssignmentUserNotReadyException;
import org.ays.auth.model.AysIdentity;
import org.ays.location.util.AysLocationUtil;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.repository.UserRepository;
import org.ays.user.util.exception.AysUserNotExistByIdException;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AssignmentSearchServiceImpl implements AssignmentSearchService {
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final AysIdentity identity;

    private final AssignmentEntityToAssignmentMapper assignmentEntityToAssignmentMapper = AssignmentEntityToAssignmentMapper.initialize();

    /**
     * Retrieves nearest assigment by their point
     *
     * @param searchRequest the AssignmentSearchRequest
     * @return Assignment
     */
    @Override
    public Assignment searchAssignment(AssignmentSearchRequest searchRequest) {
        final String userId = identity.getUserId();
        final String institutionId = identity.getInstitutionId();
        final UserEntity userEntity = userRepository
                .findByIdAndInstitutionId(userId, institutionId)
                .orElseThrow(() -> new AysUserNotExistByIdException(userId));

        assignmentRepository
                .findByUserIdAndStatusNot(userId, AssignmentStatus.DONE)
                .ifPresent(assignment -> {
                    throw new AysAssignmentUserAlreadyAssigned(userId, assignment.getId());
                });

        if (!userEntity.isReady()) {
            throw new AysAssignmentUserNotReadyException(userId, institutionId);
        }

        final Double longitude = searchRequest.getLongitude();
        final Double latitude = searchRequest.getLatitude();
        final Point searchRequestPoint = AysLocationUtil.generatePoint(longitude, latitude);
        AssignmentEntity assignmentEntity = assignmentRepository
                .findNearestAvailableAssignment(searchRequestPoint, institutionId)
                .orElseThrow(() -> new AysAssignmentNotExistByPointException(longitude, latitude));

        assignmentEntity.reserve(userId, institutionId);
        assignmentRepository.save(assignmentEntity);

        return assignmentEntityToAssignmentMapper.map(assignmentEntity);
    }
}
