package com.ays.admin_user.util.exception;

import com.ays.common.util.exception.AysNotExistException;

public class AysAdminUserRegisterVerificationCodeNotValidException extends AysNotExistException {

    public AysAdminUserRegisterVerificationCodeNotValidException(String verificationId) {
        super("VERIFICATION ID IS NOT VALID! verificationId:" + verificationId);
    }

}
