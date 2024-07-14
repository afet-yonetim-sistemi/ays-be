package org.ays.common.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AysMailTemplate {

    @Deprecated(forRemoval = true, since = "This value will be removed, when new values are added to the enum.")
    EXAMPLE("create-password.html"),
    CREATE_PASSWORD("create-password.html");

    private final String file;

}