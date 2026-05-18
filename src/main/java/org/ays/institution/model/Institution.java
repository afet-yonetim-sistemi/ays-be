package org.ays.institution.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.BaseDomainModel;
import org.ays.institution.model.enums.InstitutionStatus;

/**
 * Institution Domain Model to perform data transfer from service layer to controller
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
public class Institution extends BaseDomainModel {

    private String id;
    private String name;
    private String feUrl;
    private InstitutionStatus status;

    /**
     * Determines if the institution is active based on its status.
     *
     * @return {@code true} if the institution has a status of {@code InstitutionStatus.ACTIVE},
     *         otherwise {@code false}.
     */
    @JsonIgnore
    public boolean isActive() {
        return this.status == InstitutionStatus.ACTIVE;
    }

}
