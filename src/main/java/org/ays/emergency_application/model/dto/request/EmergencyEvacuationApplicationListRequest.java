package org.ays.emergency_application.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.common.model.AysFiltering;
import org.ays.common.model.dto.request.AysFilteringRequest;
import org.ays.common.model.dto.request.AysPagingRequest;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

/**
 * A request class for handling the pagination and filtering of emergency evacuation applications.
 * Extends {@link AysPagingRequest} and implements {@link AysFilteringRequest}.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class EmergencyEvacuationApplicationListRequest extends AysPagingRequest implements AysFilteringRequest {

    @Valid
    private Filter filter;

    /**
     * Represents a filtering configuration for admin registration applications based on the class fields.
     */
    @Getter
    @Setter
    public static class Filter implements AysFiltering {

        @Size(min = 1, max = 10)
        private String referenceNumber;

        @Size(min = 2, max = 100)
        private String sourceCity;

        @Size(min = 2, max = 100)
        private String sourceDistrict;

        @Range(min = 1, max = 999)
        private Integer seatingCount;

        @Size(min = 2, max = 100)
        private String targetCity;

        @Size(min = 2, max = 100)
        private String targetDistrict;

        private List<EmergencyEvacuationApplicationStatus> statuses;

        private Boolean isInPerson;

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
        final Set<String> acceptedFilterFields = Set.of("createdAt");
        return this.isPropertyAccepted(acceptedFilterFields);
    }

    /**
     * Converts this request's filter configuration into a {@link Specification} for querying.
     *
     * @param clazz The class type to which the specification will be applied.
     * @param <C>   The type of the class.
     * @return A specification built based on the current filter configuration.
     */
    @Override
    public <C> Specification<C> toSpecification(Class<C> clazz) {

        if (this.filter == null) {
            return Specification.allOf();
        }

        Specification<C> specification = Specification.where(null);

        if (this.filter.referenceNumber != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("referenceNumber"), "%" + this.filter.referenceNumber + "%"));
        }

        if (this.filter.sourceCity != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("sourceCity"), "%" + this.filter.sourceCity + "%"));
        }

        if (this.filter.sourceDistrict != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("sourceDistrict"), "%" + this.filter.sourceDistrict + "%"));
        }

        if (this.filter.seatingCount != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("seatingCount"), this.filter.seatingCount));
        }

        if (this.filter.targetCity != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("targetCity"), "%" + this.filter.targetCity + "%"));
        }

        if (this.filter.targetDistrict != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("targetDistrict"), "%" + this.filter.targetDistrict + "%"));
        }

        if (!CollectionUtils.isEmpty(this.filter.statuses)) {
            Specification<C> statusSpecification = this.filter.statuses.stream()
                    .map(status -> (Specification<C>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("status"), status))
                    .reduce(Specification::or).orElse(null);

            specification = specification.and(statusSpecification);
        }

        if (this.filter.isInPerson != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isInPerson"), this.filter.isInPerson));
        }

        return specification;
    }

}
