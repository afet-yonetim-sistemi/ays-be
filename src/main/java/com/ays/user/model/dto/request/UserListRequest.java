package com.ays.user.model.dto.request;

import com.ays.common.model.dto.request.AysPagingRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;

import java.util.Set;

public class UserListRequest extends AysPagingRequest {

    @JsonIgnore
    @AssertTrue
    @Override
    public boolean isSortPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of();
        return this.isPropertyAccepted(acceptedFilterFields);
    }
}
