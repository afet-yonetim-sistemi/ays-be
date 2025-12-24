package org.ays.sos.service;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysIdentity;
import org.ays.auth.model.enums.AysTokenClaims;
import org.ays.sos.model.entity.SosEntity;
import org.ays.sos.model.request.SosRequest;
import org.ays.sos.repository.SosRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling SOS emergency requests.
 */
@Service
@RequiredArgsConstructor
public class SosService {

    private final SosRepository sosRepository;
    private final AysIdentity aysIdentity;
    private final org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

    /**
     * Create a new SOS emergency request with the user's current location.
     *
     * @param sosRequest the SOS request containing location data
     */
    @Transactional
    public void create(SosRequest sosRequest) {
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

        org.ays.sos.model.response.SosResponse response = org.ays.sos.model.response.SosResponse.builder()
                .id(savedEntity.getId())
                .userId(savedEntity.getUserId())
                .firstName(savedEntity.getFirstName())
                .lastName(savedEntity.getLastName())
                .message(savedEntity.getMessage())
                .latitude(savedEntity.getLatitude())
                .longitude(savedEntity.getLongitude())
                .createdAt(savedEntity.getCreatedAt().toInstant(java.time.ZoneOffset.UTC).toEpochMilli())
                .build();

        messagingTemplate.convertAndSend("/topic/sos", response);
    }

}
