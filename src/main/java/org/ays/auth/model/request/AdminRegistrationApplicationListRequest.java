package org.ays.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.auth.model.AdminRegistrationApplicationFilter;
import org.ays.common.model.request.AysPagingRequest;

import java.util.Set;

/**
 * Represents a request object for listing admin registration applications with pagination and filtering.
 * Extends {@link AysPagingRequest} to inherit pagination-related properties and methods.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegistrationApplicationListRequest extends AysPagingRequest {

    @Valid
    private AdminRegistrationApplicationFilter filter;

    /**
     * Overrides the {@link AysPagingRequest#isOrderPropertyAccepted()} method to validate sorting options
     * and ensures that no unsupported sorting property is used in the request.
     *
     * @return true if the sorting property is accepted, false otherwise.
     */
    @JsonIgnore
    @AssertTrue
    @Override
    public boolean isOrderPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("createdAt");
        return this.isPropertyAccepted(acceptedFilterFields);
    }

}
