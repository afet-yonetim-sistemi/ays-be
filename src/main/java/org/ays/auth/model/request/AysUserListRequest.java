package org.ays.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.auth.model.AysUserFilter;
import org.ays.common.model.request.AysPagingRequest;

import java.util.Set;

/**
 * Data transfer object for handling user listing requests with pagination and filtering capabilities.
 * <p>
 * The {@code AysUserListRequest} class extends {@link AysPagingRequest} to provide additional
 * functionalities for user listing requests, including validation of sorting properties and the
 * inclusion of a {@link AysUserFilter} for filtering users based on specific criteria.
 * </p>
 *
 * @see AysPagingRequest
 * @see AysUserFilter
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AysUserListRequest extends AysPagingRequest {


    @Valid
    private AysUserFilter filter;

    /**
     * Validates sorting properties to ensure no unsupported sorting property is used in the request.
     * <p>
     * This method overrides {@link AysPagingRequest#isOrderPropertyAccepted()} to enforce that only
     * certain sorting properties, such as "firstName" and "createdAt", are accepted for sorting users.
     * </p>
     *
     * @return {@code true} if the sorting property is accepted, {@code false} otherwise.
     */
    @JsonIgnore
    @AssertTrue
    @Override
    public boolean isOrderPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("firstName", "createdAt");
        return this.isPropertyAccepted(acceptedFilterFields);
    }
}
