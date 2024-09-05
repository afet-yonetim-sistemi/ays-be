package org.ays.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AysValidTestData {

    public static final String EMAIL_ADDRESS = "test@afetyonetimsistemi.org";
    public static final String PASSWORD = "A123y456S.";
    public static final String PASSWORD_ENCRYPTED = "$2a$10$H/lKEaKsusQztOaJmYTAi.4MAmjvnxWOh0DY.XrgwHy5D2gENVIky";

    public static class SuperAdmin {
        public static final String ID = "f882cb9c-9341-473b-a040-3fbd05c09ac6";
        public static final String INSTITUTION_ID = "c5f9f610-f1c8-48a4-b1e9-1fee95f5a51f";
        public static final String EMAIL_ADDRESS = "james.william@afetyonetimsistemi.org";
    }

    public static class Admin {
        public static final String ID = "9ebcd692-fc0b-4f76-9948-3dd246d73758";
        public static final String INSTITUTION_ID = "08cd4b85-bb28-4e4a-a322-fb7b293d97f8";
        public static final String EMAIL_ADDRESS = "kyle.joanne@afetyonetimsistemi.org";
    }

    public static class User {
        public static final String ID = "cf1587fd-f800-42f2-ac6e-bd7b1c4d993d";
        public static final String INSTITUTION_ID = "08cd4b85-bb28-4e4a-a322-fb7b293d97f8";
        public static final String EMAIL_ADDRESS = "diego.ruiz@afetyonetimsistemi.org";
    }

}
