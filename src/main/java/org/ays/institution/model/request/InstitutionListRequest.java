package org.ays.institution.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.common.model.request.AysPagingRequest;
import org.ays.institution.model.InstitutionFilter;

import java.util.Set;

/**
 * Data transfer object for handling institution listing requests with pagination and filtering capabilities.
 * <p>
 * The {@code InstitutionListRequest} class extends {@link AysPagingRequest} to provide additional
 * functionalities for institution listing requests, including validation of sorting properties and the
 * inclusion of a {@link InstitutionFilter} for filtering institutions based on specific criteria.
 * </p>
 *
 * @see AysPagingRequest
 * @see InstitutionFilter
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionListRequest extends AysPagingRequest {

    @Valid
    private InstitutionFilter filter;

    /**
     * Validates sorting properties to ensure no unsupported sorting property is used in the request.
     * <p>
     * This method overrides {@link AysPagingRequest#isOrderPropertyAccepted()} to enforce that only
     * certain sorting properties, such as "name" and "createdAt", are accepted for sorting institutions.
     * </p>
     *
     * @return {@code true} if the sorting property is accepted, {@code false} otherwise.
     */
    @JsonIgnore
    @AssertTrue
    @Override
    public boolean isOrderPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("name", "createdAt");
        return this.isPropertyAccepted(acceptedFilterFields);
    }
}
