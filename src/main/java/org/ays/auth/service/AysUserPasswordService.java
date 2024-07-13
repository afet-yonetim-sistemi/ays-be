package org.ays.auth.service;

import org.ays.auth.model.request.AysForgotPasswordRequest;

public interface AysUserPasswordService {

    void forgotPassword(AysForgotPasswordRequest forgotPasswordRequest);

}
