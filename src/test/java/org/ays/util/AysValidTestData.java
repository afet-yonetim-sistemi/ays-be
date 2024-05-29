package org.ays.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AysValidTestData {

    public static final String EMAIL = "test@afetyonetimsistemi.org";
    public static final String PASSWORD = "A123y456S.";
    public static final String PASSWORD_ENCRYPTED = "$2a$10$H/lKEaKsusQztOaJmYTAi.4MAmjvnxWOh0DY.XrgwHy5D2gENVIky";

    public static class SuperAdminUserV2 {
        public static final String ID = "f882cb9c-9341-473b-a040-3fbd05c09ac6";
        public static final String INSTITUTION_ID = "c5f9f610-f1c8-48a4-b1e9-1fee95f5a51f";
        public static final String EMAIL_ADDRESS = "james.william@afetyonetimsistemi.org";
    }

    public static class AdminV2 {
        public static final String ID = "9ebcd692-fc0b-4f76-9948-3dd246d73758";
        public static final String INSTITUTION_ID = "08cd4b85-bb28-4e4a-a322-fb7b293d97f8";
        public static final String EMAIL_ADDRESS = "kyle.joanne@afetyonetimsistemi.org";
    }

    public static class UserV2 {
        public static final String ID = "cf1587fd-f800-42f2-ac6e-bd7b1c4d993d";
        public static final String INSTITUTION_ID = "08cd4b85-bb28-4e4a-a322-fb7b293d97f8";
        public static final String EMAIL_ADDRESS = "diego.ruiz@afetyonetimsistemi.org";
    }

    @Deprecated(since = "SuperAdminUser V2 Production'a alınınca burası silinecektir.", forRemoval = true)
    public static class SuperAdminUser {
        public static final String ID = "ea29a0bd-ad5d-4068-88c3-a8835b8d555e";
        public static final String USERNAME = "ays-super-admin";
    }

    @Deprecated(since = "AdminUser V2 Production'a alınınca burası silinecektir.", forRemoval = true)
    public static class AdminUser {
        public static final String ID = "9262f0fc-93db-4f7e-81c6-aaad85c2b206";
        public static final String USERNAME = "ays-admin-1";
        public static final String PASSWORD = "A123y456S.";
        public static final String EMAIL = "admin1@ays.com";
    }

    @Deprecated(since = "User V2 Production'a alınınca burası silinecektir.", forRemoval = true)
    public static class User {
        public static final String ID = "c4b4e4db-5641-41f7-8222-a76deb1c065c";
        public static final String USERNAME = "232180";
        public static final String PASSWORD = "367894";
    }

    public static class Institution {
        public static final String ID = "77ece256-bf0e-4bbe-801d-173083f8bdcf";
    }

}
