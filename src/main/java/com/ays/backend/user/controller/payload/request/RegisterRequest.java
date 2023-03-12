package com.ays.backend.user.controller.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    private String username;
    private String password;
    private String countryCode;
    private String lineNumber;
    private String firstName;
    private String lastName;
    private String email;
    private Long organizationId;

}
