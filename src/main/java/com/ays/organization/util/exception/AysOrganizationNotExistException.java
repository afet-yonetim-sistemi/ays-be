package com.ays.organization.util.exception;

import com.ays.common.util.exception.AysNotExistException;

public class AysOrganizationNotExistException extends AysNotExistException {

    public AysOrganizationNotExistException(String id) {
        super("ORGANIZATION NOT EXIST! ID:" + id);
    }

}
