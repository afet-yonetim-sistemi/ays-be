package com.ays.backend.user.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SignUpResponse {

    private UUID userId;
}
