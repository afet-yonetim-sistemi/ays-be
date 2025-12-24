package org.ays.sos.model.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.sos.model.enums.MessageType;

/**
 * Request DTO for creating a new SOS message.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SosMessageRequest {

    @Size(max = 1000, message = "Message must be at most 1000 characters")
    private String message;

    @Size(max = 10000000, message = "Image data is too large")
    private String imageUrl;

    @Size(max = 10000000, message = "Audio data is too large")
    private String audioUrl;

    private MessageType messageType;

}
