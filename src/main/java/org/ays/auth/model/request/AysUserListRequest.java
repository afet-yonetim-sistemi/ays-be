package org.ays.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ays.auth.model.AysUserFilter;
import org.ays.common.model.request.AysPagingRequest;
import org.hibernate.validator.constraints.UUID;

import java.util.Set;

// todo javadoc

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AysUserListRequest extends AysPagingRequest {

    @UUID
    @NotEmpty
    private String institutionId;

    private AysUserFilter filter;

    @JsonIgnore
    @AssertTrue
    @Override
    public boolean isOrderPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("firstName");
        return this.isPropertyAccepted(acceptedFilterFields);
    }
}
