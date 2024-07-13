package org.ays.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AysForgotPasswordRequest {

    @Email
    @NotBlank
    private String emailAddress;

}
