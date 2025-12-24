package org.ays.sos.model.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Size(max = 500, message = "Image URL must be at most 500 characters")
    private String imageUrl;

}
