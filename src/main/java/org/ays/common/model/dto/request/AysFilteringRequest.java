package org.ays.common.model.dto.request;

import org.springframework.data.jpa.domain.Specification;

public interface AysFilteringRequest {

    <C> Specification<C> toSpecification(final Class<C> clazz);

}
