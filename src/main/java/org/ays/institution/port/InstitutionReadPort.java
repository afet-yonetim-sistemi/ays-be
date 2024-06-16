package org.ays.institution.port;

import org.ays.institution.model.Institution;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.parameter.model.AysParameter;

import java.util.List;

/**
 * A read port interface for accessing {@link AysParameter} data.
 * Defines methods to retrieve {@link AysParameter} entities based on name or name prefix.
 */
public interface InstitutionReadPort {

    List<Institution> findAllByStatusOrderByNameAsc(InstitutionStatus status);

}
