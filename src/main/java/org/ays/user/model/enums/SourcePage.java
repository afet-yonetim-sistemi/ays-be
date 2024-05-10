package org.ays.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SourcePage {

    LANDING("landing:page"),
    INSTITUTION("institution:page");

    private final String permission;

}
