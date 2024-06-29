package org.ays.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.auth.model.AysUserFilter;
import org.ays.common.model.request.AysPagingRequest;

import java.util.Set;

// todo javadoc

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AysUserListRequest extends AysPagingRequest {


    private AysUserFilter filter;

    @JsonIgnore
    @AssertTrue
    @Override
    public boolean isOrderPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("firstName", "createdAt");
        return this.isPropertyAccepted(acceptedFilterFields);
    }
}
