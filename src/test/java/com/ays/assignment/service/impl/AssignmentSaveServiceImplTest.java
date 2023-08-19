package com.ays.assignment.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.dto.request.AssignmentSaveRequestBuilder;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;


class AssignmentSaveServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentSaveServiceImpl assignmentSaveService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AysIdentity identity;

    @Test
    void givenValidAssignmentSaveRequest_whenAssignmentSaved_thenReturnSuccess() {

        // Given
        AssignmentSaveRequest mockAssignmentSaveRequest = new AssignmentSaveRequestBuilder()
                .withValidFields()
                .build();


        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields().build();

        // When

        Mockito.when(assignmentRepository.save(Mockito.any(AssignmentEntity.class)))
                .thenReturn(mockAssignmentEntity);

        Mockito.when(identity.getInstitutionId())
                .thenReturn(AysRandomUtil.generateUUID());

        // Then
        assignmentSaveService.saveAssignment(mockAssignmentSaveRequest);

        Mockito.verify(assignmentRepository, Mockito.times(1)).findAll();
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();
    }

}
