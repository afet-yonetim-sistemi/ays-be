package com.ays.assignment.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentListRequest;
import com.ays.assignment.model.dto.request.AssignmentListRequestBuilder;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequestBuilder;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentNotExistByIdException;
import com.ays.auth.model.AysIdentity;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPageBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.location.model.UserLocation;
import com.ays.location.model.entity.UserLocationEntity;
import com.ays.location.model.entity.UserLocationEntityBuilder;
import com.ays.location.model.mapper.UserLocationEntityToUserLocationMapper;
import com.ays.location.repository.UserLocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;


class AssignmentServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private UserLocationRepository userLocationRepository;

    @Mock
    private AysIdentity identity;

    private static final AssignmentEntityToAssignmentMapper ASSIGNMENT_ENTITY_TO_ASSIGNMENT_MAPPER = AssignmentEntityToAssignmentMapper.initialize();
    private static final UserLocationEntityToUserLocationMapper USER_LOCATION_ENTITY_TO_USER_LOCATION_MAPPER = UserLocationEntityToUserLocationMapper.initialize();

    @Test
    void givenAssignmentId_whenGetAssignment_thenReturnAssignment() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionId).build();
        String mockAssignmentId = mockAssignmentEntity.getId();
        UserLocationEntity mockUserLocationEntity = new UserLocationEntityBuilder().withValidFields().build();

        UserLocation mockUserLocation = USER_LOCATION_ENTITY_TO_USER_LOCATION_MAPPER.map(mockUserLocationEntity);
        Assignment mockAssignment = ASSIGNMENT_ENTITY_TO_ASSIGNMENT_MAPPER.map(mockAssignmentEntity);
        mockAssignment.getUser().setLocation(mockUserLocation.getPoint());

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(userLocationRepository.findByUserId(Mockito.anyString())).thenReturn(Optional.of(mockUserLocationEntity));

        // Then
        Assignment assignment = assignmentService.getAssignmentById(mockAssignmentId);

        Assertions.assertEquals(mockAssignment.getFirstName(), assignment.getFirstName());
        Assertions.assertEquals(mockAssignment.getDescription(), assignment.getDescription());
        Assertions.assertEquals(mockAssignment.getLastName(), assignment.getLastName());
        Assertions.assertEquals(mockAssignment.getPhoneNumber().getCountryCode(), assignment.getPhoneNumber().getCountryCode());
        Assertions.assertEquals(mockAssignment.getPhoneNumber().getLineNumber(), assignment.getPhoneNumber().getLineNumber());
        Assertions.assertEquals(mockAssignment.getStatus(), assignment.getStatus());
        Assertions.assertEquals(mockAssignment.getPoint(), assignment.getPoint());
        Assertions.assertEquals(mockAssignment.getUser().getLocation().getLongitude(),
                assignment.getUser().getLocation().getLongitude());
        Assertions.assertEquals(mockAssignment.getUser().getLocation().getLatitude(),
                assignment.getUser().getLocation().getLatitude());

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId);
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();

    }

    @Test
    void givenAssignmentId_whenAssignmentNotFound_thenThrowAysAssignmentNotExistByIdException() {

        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();
        String mockInstitutionId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAssignmentNotExistByIdException.class,
                () -> assignmentService.getAssignmentById(mockAssignmentId)
        );

        Mockito.when(assignmentRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenAssignmentListRequest_whenAssignmentStatusIsAvailable_thenReturnAysPageAssignmentResponse() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        AssignmentListRequest assignmentListRequest = new AssignmentListRequestBuilder()
                .withValidValues()
                .withPhoneNumber(null)
                .build();

        List<AssignmentEntity> mockAssignmentEntities = List.of(new AssignmentEntityBuilder()
                .withValidFields().build());
        Page<AssignmentEntity> mockPageAssignmentEntities = new PageImpl<>(mockAssignmentEntities);

        List<Assignment> mockAssignments = ASSIGNMENT_ENTITY_TO_ASSIGNMENT_MAPPER.map(mockAssignmentEntities);
        AysPage<Assignment> mockAysPageAssignments = AysPage.of(assignmentListRequest.getFilter(), mockPageAssignmentEntities, mockAssignments);

        // When
        Mockito.when(identity.getInstitutionId()).thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageAssignmentEntities);

        // Then
        AysPage<Assignment> aysPageAssignment = assignmentService.getAssignments(assignmentListRequest);

        AysPageBuilder.assertEquals(mockAysPageAssignments, aysPageAssignment);

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
    }

    @Test
    void givenAssignmentListRequest_whenPhoneNumberIsAvailable_thenReturnAysPageAssignmentResponse() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        AssignmentListRequest assignmentListRequest = new AssignmentListRequestBuilder()
                .withValidValues()
                .withStatuses(null)
                .build();

        List<AssignmentEntity> mockAssignmentEntities = List.of(new AssignmentEntityBuilder()
                .withValidFields().build());
        Page<AssignmentEntity> mockPageAssignmentEntities = new PageImpl<>(mockAssignmentEntities);

        List<Assignment> mockAssignments = ASSIGNMENT_ENTITY_TO_ASSIGNMENT_MAPPER.map(mockAssignmentEntities);
        AysPage<Assignment> mockAysPageAssignments = AysPage.of(assignmentListRequest.getFilter(), mockPageAssignmentEntities, mockAssignments);

        // When
        Mockito.when(identity.getInstitutionId()).thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageAssignmentEntities);

        // Then
        AysPage<Assignment> aysPageAssignment = assignmentService.getAssignments(assignmentListRequest);

        AysPageBuilder.assertEquals(mockAysPageAssignments, aysPageAssignment);

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
    }

    @Test
    void givenAssignmentListRequest_whenAssignmentStatusAndPhoneNumberAvailable_thenReturnAysPageAssignmentResponse() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        AssignmentListRequest assignmentListRequest = new AssignmentListRequestBuilder()
                .withValidValues()
                .build();

        List<AssignmentEntity> mockAssignmentEntities = List.of(new AssignmentEntityBuilder()
                .withValidFields().build());
        Page<AssignmentEntity> mockPageAssignmentEntities = new PageImpl<>(mockAssignmentEntities);

        List<Assignment> mockAssignments = ASSIGNMENT_ENTITY_TO_ASSIGNMENT_MAPPER.map(mockAssignmentEntities);
        AysPage<Assignment> mockAysPageAssignments = AysPage.of(assignmentListRequest.getFilter(), mockPageAssignmentEntities, mockAssignments);

        // When
        Mockito.when(identity.getInstitutionId()).thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(mockPageAssignmentEntities);

        // Then
        AysPage<Assignment> aysPageAssignment = assignmentService.getAssignments(assignmentListRequest);

        AysPageBuilder.assertEquals(mockAysPageAssignments, aysPageAssignment);

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
    }


    @Test
    void givenValidAssignmentIdAndAssignmentUpdateRequest_whenAssignmentAvailable_thenUpdateAssignment() {

        // Given
        final String mockInstitutionId = AysRandomUtil.generateUUID();
        final String mockAssignmentId = AysRandomUtil.generateUUID();

        final AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder().withValidFields().build();

        final AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withId(mockAssignmentId)
                .withInstitutionId(mockInstitutionId)
                .withStatus(AssignmentStatus.AVAILABLE)
                .build();

        // When
        Mockito.when(identity.getInstitutionId()).thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(assignmentRepository.save(mockAssignmentEntity)).thenReturn(mockAssignmentEntity);

        // Then
        assignmentService.updateAssignment(mockAssignmentId, mockUpdateRequest);

        Assertions.assertEquals(mockUpdateRequest.getDescription(), mockAssignmentEntity.getDescription());
        Assertions.assertEquals(mockUpdateRequest.getFirstName(), mockAssignmentEntity.getFirstName());
        Assertions.assertEquals(mockUpdateRequest.getLastName(), mockAssignmentEntity.getLastName());
        Assertions.assertEquals(mockUpdateRequest.getPhoneNumber().getCountryCode(), mockAssignmentEntity.getCountryCode());
        Assertions.assertEquals(mockUpdateRequest.getLongitude(), mockAssignmentEntity.getPoint().getX());
        Assertions.assertEquals(mockUpdateRequest.getLatitude(), mockAssignmentEntity.getPoint().getY());

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId);
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .save(Mockito.any(AssignmentEntity.class));
    }

    @Test
    void givenValidAssignmentIdAndAssignmentUpdateRequest_whenAssignmentNotAvailable_thenThrowAysAssignmentNotExistByIdException() {

        // Given
        final String mockInstitutionId = AysRandomUtil.generateUUID();
        final String mockAssignmentId = AysRandomUtil.generateUUID();

        final AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder().withValidFields().build();

        final AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withId(mockAssignmentId)
                .withInstitutionId(mockInstitutionId)
                .withStatus(AssignmentStatus.ASSIGNED)
                .build();

        // When
        Mockito.when(identity.getInstitutionId()).thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.when(assignmentRepository.save(mockAssignmentEntity)).thenReturn(mockAssignmentEntity);

        // Then
        Assertions.assertThrows(AysAssignmentNotExistByIdException.class,
                () -> assignmentService.updateAssignment(mockAssignmentId, mockUpdateRequest)
        );

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId);
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
        Mockito.verify(assignmentRepository, Mockito.never()).save(Mockito.any(AssignmentEntity.class));
    }

    @Test
    void givenValidAssignmentIdAndAssignmentUpdateRequest_whenAssignmentNotExists_thenThrowAysAssignmentNotExistByIdException() {

        // Given
        final String mockInstitutionId = AysRandomUtil.generateUUID();
        final String mockAssignmentId = AysRandomUtil.generateUUID();

        final AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder().withValidFields().build();

        // When
        Mockito.when(identity.getInstitutionId()).thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(AysAssignmentNotExistByIdException.class,
                () -> assignmentService.updateAssignment(mockAssignmentId, mockUpdateRequest)
        );

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId);
        Mockito.verify(assignmentRepository, Mockito.never()).save(Mockito.any(AssignmentEntity.class));
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidAssignmentId_whenAssignmentAvailable_thenDeleteAssignment() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();
        String mockAssignmentId = AysRandomUtil.generateUUID();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.AVAILABLE)
                .build();

        // When
        Mockito.when(identity.getInstitutionId()).thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId))
                .thenReturn(Optional.of(mockAssignmentEntity));
        Mockito.doNothing().when(assignmentRepository).delete(mockAssignmentEntity);

        // Then
        assignmentService.deleteAssignment(mockAssignmentId);

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId);
        Mockito.verify(assignmentRepository, Mockito.times(1))
                .delete(Mockito.any(AssignmentEntity.class));
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidAssignmentId_whenAssignmentNotAvailable_thenThrowAysAssignmentNotExistsById() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();
        String mockAssignmentId = AysRandomUtil.generateUUID();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.IN_PROGRESS)
                .build();

        // When
        Mockito.when(identity.getInstitutionId()).thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAssignmentNotExistByIdException.class,
                () -> assignmentService.deleteAssignment(mockAssignmentId)
        );

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId);
        Mockito.verify(assignmentRepository, Mockito.never())
                .delete(mockAssignmentEntity);
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }

    @Test
    void givenValidAssignmentId_whenAssignmentNotExists_thenThrowAysAssignmentNotExistsById() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();
        String mockAssignmentId = AysRandomUtil.generateUUID();

        // When
        Mockito.when(identity.getInstitutionId()).thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(
                AysAssignmentNotExistByIdException.class,
                () -> assignmentService.deleteAssignment(mockAssignmentId)
        );

        Mockito.verify(assignmentRepository, Mockito.times(1))
                .findByIdAndInstitutionId(mockAssignmentId, mockInstitutionId);
        Mockito.verify(assignmentRepository, Mockito.never())
                .delete(Mockito.any(AssignmentEntity.class));
        Mockito.verify(identity, Mockito.times(1))
                .getInstitutionId();
    }
}
