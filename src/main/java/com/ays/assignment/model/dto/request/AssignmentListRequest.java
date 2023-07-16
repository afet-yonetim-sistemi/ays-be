package com.ays.assignment.model.dto.request;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.dto.request.AysFilteringRequest;
import com.ays.common.model.dto.request.AysPagingRequest;
import com.ays.common.util.validation.EnumValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents a request object for fetching a list of assignment with pagination,sorting and filtering options
 * This class extends the {@link AysPagingRequest} class and adds additional validation rules for sorting.
 */
@Data
@Builder
public class AssignmentListRequest extends AysPagingRequest implements AysFilteringRequest {

    private AssignmentStatus status;

    /**
     * Checks if the assignment status is valid.
     *
     * @return true if the assignment status is valid or null, false otherwise.
     */
    @AssertTrue(message = "IS ASSIGNMENT STATUS NOT VALID")
    private boolean isStatusValid() {

        if (this.status == null) {
            return true;
        }

        EnumSet<AssignmentStatus> acceptedAssignmentStatuses = EnumSet.of(AssignmentStatus.AVAILABLE,
                AssignmentStatus.RESERVED,
                AssignmentStatus.ASSIGNED,
                AssignmentStatus.IN_PROGRESS,
                AssignmentStatus.DONE
        );
        return EnumValidation.anyOf(this.status, acceptedAssignmentStatuses);
    }

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

    /**
     * Converts the request into a JPA Specification that filters assignments based on the specified status,
     * if it is provided.
     *
     * @param clazz the class type of the specification.
     * @return the generated JPA Specification based on the request filters.
     */
    @Override
    public <C> Specification<C> toSpecification(Class<C> clazz) {
        Specification<C> specification = Specification.where(null);
        if (status != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("status"), status));
        }
        // Add more filter conditions if needed
        return specification;
    }

}
