package com.ays.assignment.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.assignment.util.exception.AysAssignmentNotExistByIdException;
import com.ays.auth.model.AysIdentity;
import com.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;


class AssignmentServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AysIdentity identity;

    private static final AssignmentEntityToAssignmentMapper ASSIGNMENT_ENTITY_TO_ASSIGNMENT_MAPPER = AssignmentEntityToAssignmentMapper.initialize();


    @Test
    void givenAssignmentId_whenGetAssignment_thenReturnAssignment() {

        // Given
        String mockInstitutionId = AysRandomUtil.generateUUID();

        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionId).build();
        String mockAssignmentId = mockAssignmentEntity.getId();

        Assignment mockAssignment = ASSIGNMENT_ENTITY_TO_ASSIGNMENT_MAPPER.map(mockAssignmentEntity);

        // When
        Mockito.when(identity.getInstitutionId())
                .thenReturn(mockInstitutionId);
        Mockito.when(assignmentRepository.findByIdAndInstitutionId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(mockAssignmentEntity));

        // Then
        Assignment assignment = assignmentService.getAssignmentById(mockAssignmentId);

        Assertions.assertEquals(mockAssignment.getFirstName(), assignment.getFirstName());
        Assertions.assertEquals(mockAssignment.getDescription(), assignment.getDescription());
        Assertions.assertEquals(mockAssignment.getLastName(), assignment.getLastName());
        Assertions.assertEquals(mockAssignment.getPhoneNumber().getCountryCode(), assignment.getPhoneNumber().getCountryCode());
        Assertions.assertEquals(mockAssignment.getPhoneNumber().getLineNumber(), assignment.getPhoneNumber().getLineNumber());
        Assertions.assertEquals(mockAssignment.getStatus(), assignment.getStatus());
        Assertions.assertEquals(mockAssignment.getPoint(), assignment.getPoint());

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
}