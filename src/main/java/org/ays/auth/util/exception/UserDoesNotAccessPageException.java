package org.ays.auth.util.exception;

import org.ays.auth.model.enums.SourcePage;
import org.ays.common.util.exception.AysAuthException;

import java.io.Serial;

public final class UserDoesNotAccessPageException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -1433927234948182106L;

    public UserDoesNotAccessPageException(String userId, SourcePage sourcePage) {
        super("user not allowed to access the source page! userId:" + userId + " sourcePage:" + sourcePage);
    }

}
