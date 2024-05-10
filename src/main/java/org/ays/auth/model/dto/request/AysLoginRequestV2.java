package org.ays.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ays.user.model.enums.SourcePage;

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
