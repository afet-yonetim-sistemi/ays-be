package com.ays.user.service.impl;

import com.ays.auth.model.AysToken;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.enums.AysTokenClaims;
import com.ays.auth.service.AysTokenService;
import com.ays.auth.util.exception.PasswordNotValidException;
import com.ays.auth.util.exception.UserNotActiveException;
import com.ays.auth.util.exception.UsernameNotValidException;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.repository.UserRepository;
import com.ays.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;

    private final AysTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AysToken authenticate(AysLoginRequest loginRequest) {

        final UserEntity userEntity = this.findUser(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            throw new PasswordNotValidException();
        }

        return tokenService.generate(userEntity.getClaims());

    }

    public AysToken refreshAccessToken(final String refreshToken) {

        tokenService.verifyAndValidate(refreshToken);
        final String username = tokenService
                .getClaims(refreshToken)
                .get(AysTokenClaims.USERNAME.getValue()).toString();

        final UserEntity userEntity = this.findUser(username);

        return tokenService.generate(userEntity.getClaims(), refreshToken);
    }


    private UserEntity findUser(String username) {
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotValidException(username));

        if (!userEntity.isActive()) {
            throw new UserNotActiveException(username);
        }

        return userEntity;
    }
}
