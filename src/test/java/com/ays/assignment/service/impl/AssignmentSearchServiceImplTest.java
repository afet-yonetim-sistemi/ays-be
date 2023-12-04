package com.ays.assignment.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.dto.request.AssignmentSearchRequest;
import com.ays.assignment.model.dto.request.AssignmentSearchRequestBuilder;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentNotExistByPointException;
import com.ays.assignment.util.exception.AysAssignmentUserAlreadyAssigned;
import com.ays.assignment.util.exception.AysAssignmentUserNotReadyException;
import com.ays.auth.model.AysIdentity;
import com.ays.location.util.AysLocationUtil;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class AssignmentSearchServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentSearchServiceImpl assignmentSearchService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AysIdentity identity;

    @Test
    void givenValidAssignmentSearchRequest_whenAssignmentSearched_thenReturnAssignment() {

        // Given
        AssignmentSearchRequest mockAssignmentSearchRequest = new AssignmentSearchRequestBuilder()
                .withValidFields()
                .build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withSupportStatus(UserSupportStatus.READY)
                .build();
        Point mockAssignmentPoint = AysLocationUtil.generatePoint(
                mockAssignmentSearchRequest.getLongitude(),
                mockAssignmentSearchRequest.getLatitude()
        );
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withPoint(mockAssignmentPoint)
                .build();

        // When
        Mockito.when(identity.getUserId()).thenReturn(mockUserEntity.getId());
        Mockito.when(identity.getInstitutionId()).thenReturn(mockUserEntity.getInstitutionId());

        Mockito.when(userRepository.findByIdAndInstitutionId(
                        mockUserEntity.getId(),
                        mockUserEntity.getInstitutionId()
                ))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(assignmentRepository.findNearestAvailableAssignment(
                        mockAssignmentPoint,
                        mockUserEntity.getInstitutionId()
                ))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(assignmentRepository.save(mockAssignmentEntity))
                .thenReturn(mockAssignmentEntity);

        // Then
        assignmentSearchService.searchAssignment(mockAssignmentSearchRequest);

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockUserEntity.getId(), mockUserEntity.getInstitutionId());
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusNot(mockUserEntity.getId(), AssignmentStatus.DONE);
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findNearestAvailableAssignment(mockAssignmentPoint, mockUserEntity.getInstitutionId());
        Mockito.verify(assignmentRepository, Mockito.times(1)).save(mockAssignmentEntity);
    }

    @Test
    void givenValidAssignmentSearchRequest_whenUserIsNotReady_thenThrowAysAssignmentUserNotReadyException() {

        // Given
        AssignmentSearchRequest mockAssignmentSearchRequest = new AssignmentSearchRequestBuilder()
                .withValidFields()
                .build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .build();
        Point mockAssignmentPoint = AysLocationUtil.generatePoint(
                mockAssignmentSearchRequest.getLongitude(),
                mockAssignmentSearchRequest.getLatitude()
        );
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withPoint(mockAssignmentPoint)
                .build();

        // When
        Mockito.when(identity.getUserId()).thenReturn(mockUserEntity.getId());
        Mockito.when(identity.getInstitutionId()).thenReturn(mockUserEntity.getInstitutionId());
        Mockito.when(assignmentRepository.findByUserIdAndStatusNot(mockUserEntity.getId(), AssignmentStatus.DONE))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByIdAndInstitutionId(
                        mockUserEntity.getId(),
                        mockUserEntity.getInstitutionId()
                ))
                .thenReturn(Optional.of(mockUserEntity));


        // Then
        Assertions.assertThrows(
                AysAssignmentUserNotReadyException.class,
                () -> assignmentSearchService.searchAssignment(mockAssignmentSearchRequest)
        );
        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockUserEntity.getId(), mockUserEntity.getInstitutionId());
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusNot(mockUserEntity.getId(), AssignmentStatus.DONE);
        Mockito.verify(assignmentRepository, Mockito.times(0))
                .findNearestAvailableAssignment(mockAssignmentPoint, mockUserEntity.getInstitutionId());
        Mockito.verify(assignmentRepository, Mockito.times(0)).save(mockAssignmentEntity);
    }

    @Test
    void givenInValidAssignmentSearchRequest_whenAssignmentNotExistForSearching_thenThrowAysAssignmentNotExistByPointException() {
        // Given
        AssignmentSearchRequest mockAssignmentSearchRequest = new AssignmentSearchRequestBuilder()
                .withValidFields()
                .build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withSupportStatus(UserSupportStatus.READY)
                .build();
        Point mockAssignmentPoint = AysLocationUtil.generatePoint(
                mockAssignmentSearchRequest.getLongitude(),
                mockAssignmentSearchRequest.getLatitude()
        );
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withPoint(mockAssignmentPoint)
                .build();

        // When
        Mockito.when(identity.getUserId()).thenReturn(mockUserEntity.getId());
        Mockito.when(identity.getInstitutionId()).thenReturn(mockUserEntity.getInstitutionId());

        Mockito.when(userRepository.findByIdAndInstitutionId(
                        mockUserEntity.getId(),
                        mockUserEntity.getInstitutionId()
                ))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(assignmentRepository.findNearestAvailableAssignment(
                        mockAssignmentPoint,
                        mockUserEntity.getInstitutionId()
                ))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAssignmentNotExistByPointException.class,
                () -> assignmentSearchService.searchAssignment(mockAssignmentSearchRequest)
        );

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockUserEntity.getId(), mockUserEntity.getInstitutionId());
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusNot(mockUserEntity.getId(), AssignmentStatus.DONE);
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findNearestAvailableAssignment(mockAssignmentPoint, mockUserEntity.getInstitutionId());
        Mockito.verify(assignmentRepository, Mockito.times(0)).save(mockAssignmentEntity);
    }

    @Test
    void givenValidAssignmentSearchRequest_whenUserAlreadyHasAssignment_thenThrowAysAssignmentUserAlreadyAssigned() {
        // Given
        AssignmentSearchRequest mockAssignmentSearchRequest = new AssignmentSearchRequestBuilder()
                .withValidFields()
                .build();

        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .build();

        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.when(identity.getUserId()).thenReturn(mockUserEntity.getId());
        Mockito.when(identity.getInstitutionId()).thenReturn(mockUserEntity.getInstitutionId());

        Mockito.when(userRepository.findByIdAndInstitutionId(
                        mockUserEntity.getId(),
                        mockUserEntity.getInstitutionId()
                ))
                .thenReturn(Optional.of(mockUserEntity));

        Mockito.when(assignmentRepository.findByUserIdAndStatusNot(
                        mockUserEntity.getId(),
                        AssignmentStatus.DONE
                ))
                .thenReturn(Optional.of(mockAssignmentEntity));

        // Then
        Assertions.assertThrows(
                AysAssignmentUserAlreadyAssigned.class,
                () -> assignmentSearchService.searchAssignment(mockAssignmentSearchRequest)
        );

        Mockito.verify(identity, Mockito.times(1)).getUserId();
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
        Mockito.verify(userRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockUserEntity.getId(), mockUserEntity.getInstitutionId());
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByUserIdAndStatusNot(mockUserEntity.getId(), AssignmentStatus.DONE);
        Mockito.verify(assignmentRepository, Mockito.never())
                .findNearestAvailableAssignment(Mockito.any(Point.class), Mockito.anyString());
        Mockito.verify(assignmentRepository, Mockito.never()).save(Mockito.any(AssignmentEntity.class));
    }

}
