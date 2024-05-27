package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;


// TODO : Add Javadoc
public class AysRoleAlreadyExistsByNameException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -8192753469109678221L;

    // TODO : Add Javadoc
    public AysRoleAlreadyExistsByNameException(String name) {
        super("ROLE ALREADY EXIST! name:" + name);
    }

}
