package com.ays.auth.util;

public class HeaderHelper {

    public static String AuthHeaderHelper(String token){
        return String.format("Bearer %s",token);
    }

}
