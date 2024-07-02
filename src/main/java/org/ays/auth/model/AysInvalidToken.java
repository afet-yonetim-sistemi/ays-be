package org.ays.auth.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.BaseDomainModel;

/**
 * Represents an invalid token entity in the system.
 * This class extends {@link BaseDomainModel} to inherit common properties such as ID and auditing fields.
 * It encapsulates information about an invalid token that has been flagged within the system.
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AysInvalidToken extends BaseDomainModel {

    private Long id;
    private String tokenId;

}
