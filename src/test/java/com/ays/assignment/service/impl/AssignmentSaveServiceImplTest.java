package com.ays.assignment.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.dto.request.AssignmentSaveRequestBuilder;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.common.util.AysRandomUtil;
import com.ays.user.util.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;


class AssignmentSaveServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentSaveServiceImpl assignmentSaveService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AysIdentity identity;

    @Test
    void givenValidAssignmentSaveRequest_whenAssignmentSaved_thenReturnAssignment() {

        // Given
        AssignmentSaveRequest mockAssignmentSaveRequest = new AssignmentSaveRequestBuilder()
                .withValidFields()
                .build();


        List<AssignmentEntity> assignmentsFromDatabase = AssignmentEntityBuilder.generateValidAssignmentEntities(10);

        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields().build();

        // When
        Mockito.when(assignmentRepository.findAll())
                .thenReturn(assignmentsFromDatabase);

        Mockito.when(assignmentRepository.save(Mockito.any(AssignmentEntity.class)))
                .thenReturn(mockAssignmentEntity);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        // Then
        Assignment assignment = assignmentSaveService.saveAssignment(mockAssignmentSaveRequest);

        Assertions.assertEquals(mockAssignmentSaveRequest.getFirstName(), assignment.getFirstName());
        Assertions.assertEquals(mockAssignmentSaveRequest.getLastName(), assignment.getLastName());
        Assertions.assertEquals(mockAssignmentSaveRequest.getPhoneNumber().getLineNumber(), assignment.getPhoneNumber().getLineNumber());
        Assertions.assertEquals(mockAssignmentSaveRequest.getPhoneNumber().getCountryCode(), assignment.getPhoneNumber().getCountryCode());
        Assertions.assertEquals(mockAssignmentSaveRequest.getDescription(), assignment.getDescription());
        Assertions.assertEquals(mockAssignmentSaveRequest.getDescription(), assignment.getDescription());
        Assertions.assertEquals(mockAssignmentSaveRequest.getLongitude(), assignment.getLongitude());
        Assertions.assertEquals(mockAssignmentSaveRequest.getLatitude(), assignment.getLatitude());

        Mockito.verify(assignmentRepository, Mockito.times(1)).findAll();
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
    }

    @Test
    void givenAssignmentSaveRequestWithExistingPhoneNumber_whenAssignmentSaved_thenThrowAysUserAlreadyExistsByPhoneNumberException() {

        // Given
        AssignmentSaveRequest mockAssignmentSaveRequest = new AssignmentSaveRequestBuilder()
                .withValidFields()
                .build();

        // When
        List<AssignmentEntity> assignmentsFromDatabase = AssignmentEntityBuilder.generateValidAssignmentEntities(10);
        assignmentsFromDatabase.get(0).setCountryCode(mockAssignmentSaveRequest.getPhoneNumber().getCountryCode());
        assignmentsFromDatabase.get(0).setLineNumber(mockAssignmentSaveRequest.getPhoneNumber().getLineNumber());
        Mockito.when(assignmentRepository.findAll())
                .thenReturn(assignmentsFromDatabase);

        // Then
        Assertions.assertThrows(
                AysUserAlreadyExistsByPhoneNumberException.class,
                () -> assignmentSaveService.saveAssignment(mockAssignmentSaveRequest)
        );

        Mockito.verify(assignmentRepository, Mockito.times(1)).findAll();
    }

}
