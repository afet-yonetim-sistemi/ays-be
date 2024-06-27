package org.ays.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.auth.model.AysRoleFilter;
import org.ays.common.model.request.AysPagingRequest;

import java.util.Set;

/**
 * Data transfer object for handling role listing requests with pagination and filtering capabilities.
 * <p>
 * The {@code AysRoleListRequest} class extends {@link AysPagingRequest} to provide additional
 * functionalities for role listing requests, including validation of sorting properties and the
 * inclusion of a {@link AysRoleFilter} for filtering roles based on specific criteria.
 * </p>
 *
 * @see AysPagingRequest
 * @see AysRoleFilter
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AysRoleListRequest extends AysPagingRequest {

    @Valid
    private AysRoleFilter filter;

    /**
     * Validates sorting properties to ensure no unsupported sorting property is used in the request.
     * <p>
     * This method overrides {@link AysPagingRequest#isOrderPropertyAccepted()} to enforce that only
     * certain sorting properties, such as "createdAt", are accepted for sorting roles.
     * </p>
     *
     * @return {@code true} if the sorting property is accepted, {@code false} otherwise.
     */
    @JsonIgnore
    @AssertTrue
    @Override
    public boolean isOrderPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("createdAt");
        return this.isPropertyAccepted(acceptedFilterFields);
    }

}
