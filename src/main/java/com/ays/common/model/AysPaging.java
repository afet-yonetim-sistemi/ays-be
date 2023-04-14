package com.ays.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class AysPaging {

    @NotNull
    @Range(min = 1)
    public Long page;

    @NotNull
    @Range(min = 10, max = 10)
    public Long pageSize;

    public Long getPage() {
        return this.page - 1;
    }
}
