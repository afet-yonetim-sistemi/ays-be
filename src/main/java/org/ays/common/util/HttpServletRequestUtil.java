package org.ays.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpServletRequestUtil {

    public static String getClientIpAddress(final HttpServletRequest httpServletRequest) {

        final String ipAddress = httpServletRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            return httpServletRequest.getRemoteAddr().trim();
        }

        return ipAddress.split(",")[0].trim();
    }

}
