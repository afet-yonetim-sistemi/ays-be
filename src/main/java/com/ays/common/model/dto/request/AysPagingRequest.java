package com.ays.common.model.dto.request;

import com.ays.common.model.AysPaging;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * An abstract base class for paging requests that extends the {@link AysSortingRequest} class.
 * It includes a {@link AysPaging} object to define pagination parameters, and provides a method to
 * convert these parameters to a Spring {@link Pageable} object.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class AysPagingRequest extends AysSortingRequest {

    /**
     * The pagination parameters for this request.
     */
    @Valid
    @NotNull
    protected AysPaging pagination;

    /**
     * Converts the pagination parameters of this request to a Spring {@link Pageable} object.
     *
     * @return The {@link Pageable} object corresponding to this request's pagination parameters.
     */
    public Pageable toPageable() {

        if (super.isSortable()) {
            return PageRequest.of(
                    this.pagination.getPage() - 1,
                    this.pagination.getPageSize(),
                    super.toSort()
            );
        }

        return PageRequest.of(
                this.pagination.getPage() - 1,
                this.pagination.getPageSize()
        );
    }
}
