package org.ays.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AysValidTestData {

    public static final String EMAIL = "test@afetyonetimsistemi.org";
    public static final String PASSWORD = "A123y456S.";
    public static final String PASSWORD_ENCRYPTED = "$2a$10$H/lKEaKsusQztOaJmYTAi.4MAmjvnxWOh0DY.XrgwHy5D2gENVIky";

    public static class SuperAdminUser {
        public static final String ID = "ea29a0bd-ad5d-4068-88c3-a8835b8d555e";
        public static final String USERNAME = "ays-super-admin";
    }

    public static class AdminUser {
        public static final String ID = "9262f0fc-93db-4f7e-81c6-aaad85c2b206";
        public static final String USERNAME = "ays-admin-1";
        public static final String PASSWORD = "A123y456S.";
        public static final String EMAIL = "admin1@ays.com";
    }

    public static class User {
        public static final String ID = "c4b4e4db-5641-41f7-8222-a76deb1c065c";
        public static final String USERNAME = "232180";
        public static final String PASSWORD = "367894";
    }

    public static class Institution {
        public static final String ID = "77ece256-bf0e-4bbe-801d-173083f8bdcf";
    }

}
