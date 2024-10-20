package org.ays.parameter.exception;

import org.ays.common.util.exception.AysNotExistException;
import org.ays.parameter.model.AysParameter;

import java.io.Serial;

/**
 * Exception thrown when a specified {@link AysParameter} does not exist.
 * Extends {@link AysNotExistException} to indicate the absence of a parameter entity.
 */
public final class AysParameterNotExistException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -4279786521082293747L;

    /**
     * Constructs a new {@code AysParameterNotExistException} with the specified parameter name.
     *
     * @param name the name of the parameter that does not exist
     */
    public AysParameterNotExistException(String name) {
        super("parameter does not exist! name:" + name);
    }

}
