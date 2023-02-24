package com.ays.backend.user.controller.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Message response returned in case of an error.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private String message;
}
