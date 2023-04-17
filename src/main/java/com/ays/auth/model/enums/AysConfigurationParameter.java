package com.ays.auth.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AysConfigurationParameter {

    AYS("AYS"),

    AUTH_ACCESS_TOKEN_EXPIRE_MINUTE("120"),
    AUTH_REFRESH_TOKEN_EXPIRE_DAY("1"),
    AUTH_TOKEN_PRIVATE_KEY(""),
    AUTH_TOKEN_PUBLIC_KEY("");

    private final String defaultValue;

}
