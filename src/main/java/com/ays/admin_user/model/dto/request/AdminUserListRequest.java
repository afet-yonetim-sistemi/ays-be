package com.ays.admin_user.model.dto.request;

import com.ays.common.model.dto.request.AysPagingRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Represents a request object for fetching a list of admin users with pagination and sorting options.
 * This class extends the {@link AysPagingRequest} class and adds additional validation rules for sorting.
 */
@Getter
@Setter
public class AdminUserListRequest extends AysPagingRequest {

    /**
     * Overrides the {@link AysPagingRequest#isSortPropertyAccepted()} method to validate sorting options
     * and ensures that no unsupported sorting property is used in the request.
     *
     * @return true if the sorting property is accepted, false otherwise.
     */
    @JsonIgnore
    @AssertTrue
    @Override
    public boolean isSortPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of();
        return this.isPropertyAccepted(acceptedFilterFields);
    }

}
