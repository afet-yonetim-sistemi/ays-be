package org.ays.sos.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO for SOS messages.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SosMessageResponse {

    private String id;
    private String sosId;
    private String senderType;
    private String senderId;
    private String message;
    private String imageUrl;
    private String audioUrl;
    private String messageType;
    private Long createdAt;

}
