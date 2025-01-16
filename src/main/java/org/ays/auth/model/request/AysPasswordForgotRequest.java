package org.ays.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.util.validation.EmailAddress;

@Getter
@Setter
public class AysPasswordForgotRequest {

    @EmailAddress
    @NotBlank
    private String emailAddress;

}
