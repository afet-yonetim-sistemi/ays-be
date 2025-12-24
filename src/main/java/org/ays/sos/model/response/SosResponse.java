package org.ays.sos.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO for SOS emergency requests sent via WebSocket.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SosResponse {

    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String message;
    private Double latitude;
    private Double longitude;
    private Long createdAt;

}
