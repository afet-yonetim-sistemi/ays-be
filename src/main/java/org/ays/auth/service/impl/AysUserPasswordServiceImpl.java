package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.request.AysForgotPasswordRequest;
import org.ays.auth.service.AysUserPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AysUserPasswordServiceImpl implements AysUserPasswordService {

    @Override
    public void forgotPassword(final AysForgotPasswordRequest forgotPasswordRequest) {
        // TODO: Implement forgot password functionality
    }

}
