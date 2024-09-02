package org.ays.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.ays.common.util.validation.Password;

@Getter
@Setter
public class AysPasswordCreateRequest {

    @NotBlank
    @Password
    private String password;

    @NotBlank
    @Password
    private String passwordRepeat;


    @JsonIgnore
    @AssertTrue(message = "passwords must be equal")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isPasswordsEqual() {

        if (StringUtils.isEmpty(this.password) || StringUtils.isEmpty(this.passwordRepeat)) {
            return true;
        }

        return this.password.equals(this.passwordRepeat);
    }

}
