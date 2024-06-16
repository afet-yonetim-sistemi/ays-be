package org.ays.auth.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumerates source pages with associated permissions.
 *
 * <p>
 * This enum provides constants for different source pages, each associated with a permission.
 *
 * <p>
 * Each constant represents a source page and its corresponding permission string.
 */
@Getter
@RequiredArgsConstructor
public enum SourcePage {

    LANDING("landing:page"),
    INSTITUTION("institution:page");

    private final String permission;

}
