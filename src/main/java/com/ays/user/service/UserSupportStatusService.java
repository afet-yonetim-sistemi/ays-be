package com.ays.user.service;

import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;

public interface UserSupportStatusService {

    void updateUserSupportStatus(UserSupportStatusUpdateRequest updateRequest);
}
