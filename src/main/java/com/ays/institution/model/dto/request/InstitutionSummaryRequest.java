package com.ays.institution.model.dto.request;

import com.ays.common.model.dto.request.AysPagingRequest;

import java.util.Set;

/**
 * Represents a request object for fetching a list of institutions with pagination and sorting options.
 */
public class InstitutionSummaryRequest extends AysPagingRequest {

    /**
     * Overrides the {@link AysPagingRequest#isSortPropertyAccepted()} method to validate sorting options
     * and ensures that no unsupported sorting property is used in the request.
     *
     * @return true if the sorting property is accepted, false otherwise.
     */
    @Override
    public boolean isSortPropertyAccepted() {
        final Set<String> acceptedSortFields = Set.of();
        return this.isPropertyAccepted(acceptedSortFields);
    }
}
