package com.ays.admin_user.util.exception;

public class AysAdminUserNotActiveException extends RuntimeException {

    public AysAdminUserNotActiveException(String username) {
        super("USER IS NOT ACTIVE! username:{}" + username);
    }

}
