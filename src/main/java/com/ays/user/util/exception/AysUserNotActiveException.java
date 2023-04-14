package com.ays.user.util.exception;

public class AysUserNotActiveException extends RuntimeException {

    public AysUserNotActiveException(String username) {
        super("USER IS NOT ACTIVE! username:{}" + username);
    }

}
