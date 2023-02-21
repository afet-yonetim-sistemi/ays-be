package com.ays.backend.user.controller.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Message response returned in case of an error.
 */
@Data
@AllArgsConstructor
public class MessageResponse {

    private String message;
}
