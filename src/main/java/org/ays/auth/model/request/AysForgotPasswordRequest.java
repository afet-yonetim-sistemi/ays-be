package org.ays.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.validation.Email;

@Getter
@Setter
public class AysForgotPasswordRequest {

    @Email
    @NotBlank
    @Size(min = 2, max = 255)
    private String emailAddress;

}
