package org.ays.common.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * BaseResponse is a base class for response objects that contain common fields for auditing purposes.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BaseResponse {

    protected String createdUser;
    protected LocalDateTime createdAt;
    protected String updatedUser;
    protected LocalDateTime updatedAt;

}
