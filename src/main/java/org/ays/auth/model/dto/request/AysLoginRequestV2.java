package org.ays.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ays.user.model.enums.SourcePage;

/**
 * Represents a login request of the Ays application.
 *
 * <p>
 * This class encapsulates the user's email address, password, and the source page from which the login request originated.
 *
 * <p>
 * It provides getter and setter methods for accessing and modifying the email address, password, and source page fields.
 */
@Getter
@Setter
public class AysLoginRequestV2 {

    @NotBlank
    private String emailAddress;

    @NotBlank
    private String password;

    @NotNull
    private SourcePage sourcePage;

}
