package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAuthException;
import org.ays.user.model.enums.SourcePage;

import java.io.Serial;

public class UserDoesNotAccessPageException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -1433927234948182106L;

    public UserDoesNotAccessPageException(String userId, SourcePage sourcePage) {
        super("USER NOT ALLOWED TO ACCESS THE SOURCE PAGE! userId:" + userId + " sourcePage:" + sourcePage);
    }

}
