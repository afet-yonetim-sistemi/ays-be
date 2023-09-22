package com.ays.assignment.service.impl;

import com.ays.AbstractUnitTest;
import com.ays.assignment.model.enums.AssignmentHandlerType;
import com.ays.assignment.service.impl.handler.AssignmentHandler;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Set;
import java.util.stream.Stream;

public class AssignmentConcludeServiceImplTest extends AbstractUnitTest {

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

}
