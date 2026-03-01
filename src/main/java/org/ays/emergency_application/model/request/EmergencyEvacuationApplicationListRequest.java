package org.ays.emergency_application.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.common.model.request.AysPagingRequest;
import org.ays.emergency_application.model.filter.EmergencyEvacuationApplicationFilter;

import java.util.Set;

/**
 * Represents a request for listing emergency evacuation applications with support for paging and filtering.
 * <p>
 * This class extends {@link AysPagingRequest} to provide paging capabilities and includes a filter for specifying
 * additional criteria specific to emergency evacuation applications.
 * </p>
 *
 * @see AysPagingRequest
 * @see EmergencyEvacuationApplicationFilter
 */
@Getter
@Setter
@NoArgsConstructor
public class EmergencyEvacuationApplicationListRequest extends AysPagingRequest {

    @Valid
    private EmergencyEvacuationApplicationFilter filter;

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
        final Set<String> acceptedFilterFields = Set.of("priority", "createdAt");
        return this.isPropertyAccepted(acceptedFilterFields);
    }

}
