package org.ays.emergency_application.model.filter;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.ays.common.model.AysFilter;
import org.ays.common.util.validation.NoSpecialCharacters;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@Getter
@Setter
public class EmergencyEvacuationApplicationFilter implements AysFilter {

    @Size(min = 1, max = 10)
    private String referenceNumber;

    @NoSpecialCharacters
    @Size(min = 2, max = 100)
    private String sourceCity;

    @NoSpecialCharacters
    @Size(min = 2, max = 100)
    private String sourceDistrict;

    @Range(min = 1, max = 999)
    private Integer seatingCount;

    @NoSpecialCharacters
    @Size(min = 2, max = 100)
    private String targetCity;

    @NoSpecialCharacters
    @Size(min = 2, max = 100)
    private String targetDistrict;

    private Set<EmergencyEvacuationApplicationStatus> statuses;

    private Boolean isInPerson;


    /**
     * Converts this request's filter configuration into a {@link Specification} for querying.
     *
     * @param clazz The class type to which the specification will be applied.
     * @param <C>   The type of the class.
     * @return A specification built based on the current filter configuration.
     */
    @Override
    public Specification<EmergencyEvacuationApplicationEntity> toSpecification() {

        Specification<EmergencyEvacuationApplicationEntity> specification = Specification.where(null);

        if (this.referenceNumber != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("referenceNumber"), "%" + this.referenceNumber + "%"));
        }

        if (this.sourceCity != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("sourceCity"), "%" + this.sourceCity + "%"));
        }

        if (this.sourceDistrict != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("sourceDistrict"), "%" + this.sourceDistrict + "%"));
        }

        if (this.seatingCount != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("seatingCount"), this.seatingCount));
        }

        if (this.targetCity != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("targetCity"), "%" + this.targetCity + "%"));
        }

        if (this.targetDistrict != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("targetDistrict"), "%" + this.targetDistrict + "%"));
        }

        if (!CollectionUtils.isEmpty(this.statuses)) {
            Specification<EmergencyEvacuationApplicationEntity> statusSpecification = this.statuses.stream()
                    .map(status -> (Specification<EmergencyEvacuationApplicationEntity>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("status"), status))
                    .reduce(Specification::or).orElse(null);

            specification = specification.and(statusSpecification);
        }

        if (this.isInPerson != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isInPerson"), this.isInPerson));
        }

        return specification;
    }

}
