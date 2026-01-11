package org.ays.institution.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.enums.InstitutionStatus;

/**
 * Institution Domain Model to perform data transfer from service layer to controller
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Institution extends BaseDomainModel {

    private String id;
    private String name;
    private String feUrl;
    private InstitutionStatus status;

    /**
     * Checks if the institution's status is active.
     *
     * @return {@code true} if the institution's status is {@link InstitutionStatus#ACTIVE}, otherwise {@code false}.
     */
    public boolean isActive() {
        return this.status == InstitutionStatus.ACTIVE;
    }
}
