package com.ays.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AysAuthErrorResponse {

    private Date time;
    private String httpStatus;
    private String header;
    private Boolean isSuccess;

}
