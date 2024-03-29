package org.ays.assignment.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.assignment.model.dto.request.AssignmentSaveRequest;
import org.ays.assignment.model.dto.request.AssignmentSaveRequestBuilder;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.entity.AssignmentEntityBuilder;
import org.ays.assignment.repository.AssignmentRepository;
import org.ays.auth.model.AysIdentity;
import org.ays.common.util.AysRandomUtil;
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

        // Verify
        Mockito.verify(assignmentRepository, Mockito.times(1)).save(Mockito.any(AssignmentEntity.class));
        Mockito.verify(identity, Mockito.times(1)).getInstitutionId();

    }

}
