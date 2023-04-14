package com.ays.common.controller.dto.request;

import com.ays.common.model.AysPaging;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AysPagingRequest extends AysSortingRequest {

    @Valid
    @NotNull
    protected AysPaging pagination;

    public Pageable toPageable() {

        if (super.isSortable()) {
            return PageRequest.of(
                    Math.toIntExact(pagination.getPage()),
                    Math.toIntExact(pagination.getPageSize()),
                    super.toSort()
            );
        }

        return PageRequest.of(
                Math.toIntExact(pagination.getPage()),
                Math.toIntExact(pagination.getPageSize())
        );
    }
}
