package com.ays.backend.user.controller.payload.request;

import lombok.Data;

@Data
public class PaginationRequest {

    private Integer page;
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize - 1;
    }
}
