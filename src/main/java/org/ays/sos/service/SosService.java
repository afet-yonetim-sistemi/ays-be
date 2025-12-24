package org.ays.sos.service;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.sos.model.entity.SosEntity;
import org.ays.sos.model.entity.SosMessageEntity;
import org.ays.sos.model.enums.SenderType;
import org.ays.sos.model.request.SosMessageRequest;
import org.ays.sos.model.request.SosRequest;
import org.ays.sos.model.response.SosMessageResponse;
import org.ays.sos.model.response.SosResponse;
import org.ays.sos.repository.SosMessageRepository;
import org.ays.sos.repository.SosRepository;
import org.ays.sos.model.enums.MessageType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling SOS emergency requests and messages.
 */
@Service
@RequiredArgsConstructor
public class SosService {

    private final SosRepository sosRepository;
    private final SosMessageRepository sosMessageRepository;
    private final AysIdentity aysIdentity;
    private final org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

    /**
     * Create a new SOS emergency request with the user's current location.
     *
     * @param sosRequest the SOS request containing location data
     * @return the created SOS ID
     */
    @Transactional
    public String create(SosRequest sosRequest) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String firstName = jwt.getClaim(AysTokenClaims.USER_FIRST_NAME.getValue());
        String lastName = jwt.getClaim(AysTokenClaims.USER_LAST_NAME.getValue());

        SosEntity sosEntity = SosEntity.builder()
                .userId(aysIdentity.getUserId())
                .firstName(firstName)
                .lastName(lastName)
                .message(sosRequest.getMessage())
                .latitude(sosRequest.getLatitude())
                .longitude(sosRequest.getLongitude())
                .build();

        SosEntity savedEntity = sosRepository.save(sosEntity);

        // Create initial message from the SOS request
        if (sosRequest.getMessage() != null && !sosRequest.getMessage().isBlank()) {
            SosMessageEntity messageEntity = SosMessageEntity.builder()
                    .sosId(savedEntity.getId())
                    .senderType(SenderType.USER)
                    .senderId(aysIdentity.getUserId())
                    .message(sosRequest.getMessage())
                    .build();
            sosMessageRepository.save(messageEntity);
        }

        SosResponse response = SosResponse.builder()
                .id(savedEntity.getId())
                .userId(savedEntity.getUserId())
                .firstName(savedEntity.getFirstName())
                .lastName(savedEntity.getLastName())
                .message(savedEntity.getMessage())
                .latitude(savedEntity.getLatitude())
                .longitude(savedEntity.getLongitude())
                .createdAt(savedEntity.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                .build();

        messagingTemplate.convertAndSend("/topic/sos", response);

        return savedEntity.getId();
    }

    /**
     * Get all messages for a specific SOS request.
     *
     * @param sosId the SOS ID
     * @return list of messages
     */
    @Transactional(readOnly = true)
    public List<SosMessageResponse> getMessages(String sosId) {
        List<SosMessageEntity> messages = sosMessageRepository.findBySosIdOrderByCreatedAtAsc(sosId);
        return messages.stream()
                .map(this::toMessageResponse)
                .collect(Collectors.toList());
    }

    /**
     * Add a reply message to an SOS conversation.
     *
     * @param sosId   the SOS ID
     * @param request the message request
     * @return the created message response
     */
    @Transactional
    public SosMessageResponse addMessage(String sosId, SosMessageRequest request, SenderType senderType) {
        SosMessageEntity messageEntity = SosMessageEntity.builder()
                .sosId(sosId)
                .senderType(senderType)
                .senderId(aysIdentity.getUserId())
                .message(request.getMessage())
                .imageUrl(request.getImageUrl())
                .audioUrl(request.getAudioUrl())
                .messageType(request.getMessageType() != null ? request.getMessageType()
                        : (request.getAudioUrl() != null ? MessageType.AUDIO
                                : (request.getImageUrl() != null ? MessageType.IMAGE : MessageType.TEXT)))
                .build();

        SosMessageEntity savedMessage = sosMessageRepository.save(messageEntity);

        SosMessageResponse response = toMessageResponse(savedMessage);

        // Broadcast message to WebSocket topic
        messagingTemplate.convertAndSend("/topic/sos/" + sosId + "/messages", response);

        return response;
    }

    private SosMessageResponse toMessageResponse(SosMessageEntity entity) {
        return SosMessageResponse.builder()
                .id(entity.getId())
                .sosId(entity.getSosId())
                .senderType(entity.getSenderType().name())
                .senderId(entity.getSenderId())
                .message(entity.getMessage())
                .imageUrl(entity.getImageUrl())
                .audioUrl(entity.getAudioUrl())
                .messageType(entity.getMessageType() != null ? entity.getMessageType().name() : MessageType.TEXT.name())
                .createdAt(entity.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli())
                .build();
    }

}
