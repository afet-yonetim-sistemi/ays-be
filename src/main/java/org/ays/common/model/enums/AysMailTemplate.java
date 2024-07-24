package org.ays.common.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AysMailTemplate {

    CREATE_PASSWORD("create-password.html");

    private final String file;

}
