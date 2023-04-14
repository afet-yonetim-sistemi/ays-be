package com.ays.admin_user.util.exception;

public class AysAdminUserNotVerifiedException extends RuntimeException {

    public AysAdminUserNotVerifiedException(String username) {
        super("ADMIN USER IS NOT VERIFIED! username:" + username);
    }

}
