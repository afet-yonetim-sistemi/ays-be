package org.ays.admin_user.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.dto.request.AysPagingRequest;

import java.util.Set;

/**
 * Represents a request object for fetching a list of admin users with pagination and sorting options.
 * This class extends the {@link AysPagingRequest} class and adds additional validation rules for sorting.
 */
@Getter
@Setter
@Deprecated(since = "AdminUserListRequest V2 Production'a alınınca burası silinecektir.", forRemoval = true)
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
