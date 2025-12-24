package org.ays.sos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ays.common.model.response.AysResponse;
import org.ays.sos.model.enums.SenderType;
import org.ays.sos.model.request.SosMessageRequest;
import org.ays.sos.model.response.SosMessageResponse;
import org.ays.sos.service.SosService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing SOS messages (chat functionality).
 */
@Validated
@RestController
@RequiredArgsConstructor
class SosMessageController {

    private final SosService sosService;

    /**
     * Get all messages for an SOS conversation.
     * Accessible to authenticated mobile users.
     *
     * @param sosId the SOS ID
     * @return list of messages
     */
    @GetMapping("/api/mobile/v1/sos/{sosId}/messages")
    @PreAuthorize("isAuthenticated()")
    public AysResponse<List<SosMessageResponse>> getMessages(@PathVariable String sosId) {
        List<SosMessageResponse> messages = sosService.getMessages(sosId);
        return AysResponse.successOf(messages);
    }

    /**
     * Add a message to an SOS conversation (mobile user).
     *
     * @param sosId   the SOS ID
     * @param request the message request
     * @return the created message
     */
    @PostMapping("/api/mobile/v1/sos/{sosId}/messages")
    @PreAuthorize("isAuthenticated()")
    public AysResponse<SosMessageResponse> addUserMessage(
            @PathVariable String sosId,
            @RequestBody @Valid SosMessageRequest request) {
        SosMessageResponse response = sosService.addMessage(sosId, request, SenderType.USER);
        return AysResponse.successOf(response);
    }

    /**
     * Get all messages for an SOS conversation.
     * Accessible to institution operators.
     *
     * @param sosId the SOS ID
     * @return list of messages
     */
    @GetMapping("/api/institution/v1/sos/{sosId}/messages")
    @PreAuthorize("hasAuthority('application:evacuation:list')")
    public AysResponse<List<SosMessageResponse>> getMessagesForOperator(@PathVariable String sosId) {
        List<SosMessageResponse> messages = sosService.getMessages(sosId);
        return AysResponse.successOf(messages);
    }

    /**
     * Add a reply message to an SOS conversation (operator).
     *
     * @param sosId   the SOS ID
     * @param request the message request
     * @return the created message
     */
    @PostMapping("/api/institution/v1/sos/{sosId}/messages")
    @PreAuthorize("hasAuthority('application:evacuation:list')")
    public AysResponse<SosMessageResponse> addOperatorMessage(
            @PathVariable String sosId,
            @RequestBody @Valid SosMessageRequest request) {
        SosMessageResponse response = sosService.addMessage(sosId, request, SenderType.OPERATOR);
        return AysResponse.successOf(response);
    }

}
