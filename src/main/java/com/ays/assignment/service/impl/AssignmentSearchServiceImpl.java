package com.ays.assignment.service.impl;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentSearchRequest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.service.AssignmentSearchService;
import com.ays.assignment.util.exception.AysAssignmentNotExistByPointException;
import com.ays.assignment.util.exception.AysAssignmentUserNotReadyException;
import com.ays.auth.model.AysIdentity;
import com.ays.location.util.AysLocationUtil;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentSearchServiceImpl implements AssignmentSearchService {
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
        userRepository
                .findByIdAndInstitutionId(userId, institutionId)
                .filter(UserEntity::isReady)
                .orElseThrow(() -> new AysAssignmentUserNotReadyException(userId, institutionId));

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
