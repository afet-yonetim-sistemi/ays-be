package org.ays.assignment.service.impl;

import org.ays.AbstractUnitTest;
import org.ays.assignment.model.dto.request.AssignmentCancelRequest;
import org.ays.assignment.model.dto.request.AssignmentCancelRequestBuilder;
import org.ays.assignment.model.enums.AssignmentHandlerType;
import org.ays.assignment.service.impl.handler.AssignmentHandler;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Set;
import java.util.stream.Stream;

class AssignmentConcludeServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AssignmentConcludeServiceImpl assignmentConcludeService;

    @Mock
    private AssignmentHandler assignmentHandler;

    @Mock
    private Set<AssignmentHandler> assignmentHandlers;

    @Test
    void whenAssignmentConclude_thenApproveAssignment() {

        // When
        Mockito
                .when(assignmentHandlers.stream())
                .thenReturn(Stream.of(assignmentHandler));
        Mockito.when(assignmentHandler.type()).thenReturn(AssignmentHandlerType.APPROVE);
        Mockito.doNothing().when(assignmentHandler).handle();

        // Then
        assignmentConcludeService.approve();

        Mockito.verify(assignmentHandlers, Mockito.times(1)).stream();
        Mockito.verify(assignmentHandler, Mockito.times(1)).type();
        Mockito.verify(assignmentHandler, Mockito.times(1)).handle();
    }

    @Test
    void whenAssignmentConclude_thenStartAssignment() {

        // When
        Mockito
                .when(assignmentHandlers.stream())
                .thenReturn(Stream.of(assignmentHandler));
        Mockito.when(assignmentHandler.type()).thenReturn(AssignmentHandlerType.START);
        Mockito.doNothing().when(assignmentHandler).handle();

        // Then
        assignmentConcludeService.start();

        Mockito.verify(assignmentHandlers, Mockito.times(1)).stream();
        Mockito.verify(assignmentHandler, Mockito.times(1)).type();
        Mockito.verify(assignmentHandler, Mockito.times(1)).handle();
    }

    @Test
    void whenAssignmentConclude_thenRejectAssignment() {

        // When
        Mockito
                .when(assignmentHandlers.stream())
                .thenReturn(Stream.of(assignmentHandler));
        Mockito.when(assignmentHandler.type()).thenReturn(AssignmentHandlerType.REJECT);
        Mockito.doNothing().when(assignmentHandler).handle();

        // Then
        assignmentConcludeService.reject();

        Mockito.verify(assignmentHandlers, Mockito.times(1)).stream();
        Mockito.verify(assignmentHandler, Mockito.times(1)).type();
        Mockito.verify(assignmentHandler, Mockito.times(1)).handle();
    }

    @Test
    void whenAssignmentConclude_thenCompleteAssignment() {

        // When
        Mockito
                .when(assignmentHandlers.stream())
                .thenReturn(Stream.of(assignmentHandler));
        Mockito.when(assignmentHandler.type()).thenReturn(AssignmentHandlerType.COMPLETE);
        Mockito.doNothing().when(assignmentHandler).handle();

        // Then
        assignmentConcludeService.complete();

        Mockito.verify(assignmentHandlers, Mockito.times(1)).stream();
        Mockito.verify(assignmentHandler, Mockito.times(1)).type();
        Mockito.verify(assignmentHandler, Mockito.times(1)).handle();
    }

    @Test
    void givenAssignmentCancelRequest_whenAssignmentConclude_thenCancelAssignment() {

        // Given
        AssignmentCancelRequest cancelRequest = new AssignmentCancelRequestBuilder()
                .withValidFields().build();

        // When
        Mockito
                .when(assignmentHandlers.stream())
                .thenReturn(Stream.of(assignmentHandler));
        Mockito.when(assignmentHandler.type()).thenReturn(AssignmentHandlerType.CANCEL);
        Mockito.doNothing().when(assignmentHandler).handle();

        // Then
        assignmentConcludeService.cancel(cancelRequest);

        Mockito.verify(assignmentHandlers, Mockito.times(1)).stream();
        Mockito.verify(assignmentHandler, Mockito.times(1)).type();
        Mockito.verify(assignmentHandler, Mockito.times(1)).handle();
    }

}
