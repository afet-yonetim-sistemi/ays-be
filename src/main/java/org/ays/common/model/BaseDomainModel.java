package org.ays.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Base domain model to be used in order to pass the common fields to the domain model in the same module.
 */
@Getter
@Setter
@SuperBuilder
public abstract class BaseDomainModel {

    protected String createdUser;
    protected LocalDateTime createdAt;
    protected String updatedUser;
    protected LocalDateTime updatedAt;

}
