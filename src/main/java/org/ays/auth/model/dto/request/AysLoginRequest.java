package org.ays.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * AysLoginRequest is a POJO class that represents a request object containing the username and password fields for authentication purposes.
 * This object is typically used to send user credentials to an authentication endpoint to obtain an access token and refresh token.
 * <p>The class uses the Lombok annotations @Data and @Builder to automatically generate getters, setters, and a builder method.
 */
@Getter
@Setter
@Deprecated(since = "AysLoginRequest V2 Production'a alınınca burası silinecektir.", forRemoval = true)
public class AysLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
