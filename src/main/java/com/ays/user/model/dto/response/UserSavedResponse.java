package com.ays.user.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSavedResponse {

    private String username;
    private String password;

}
