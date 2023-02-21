package com.ays.backend.user.controller.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response returning from user controller in a correct case.
 */
@Data
@AllArgsConstructor
public class SignUpResponse {

    private String userId;
}
