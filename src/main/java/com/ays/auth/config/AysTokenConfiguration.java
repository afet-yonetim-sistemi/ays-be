package com.ays.auth.config;

import com.ays.auth.model.enums.AysConfigurationParameter;
import com.ays.auth.util.KeyConverter;
import com.ays.parameter.model.AysParameter;
import com.ays.parameter.service.AysParameterService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Getter
@Configuration
public class AysTokenConfiguration {

    private final String issuer;
    private final Integer accessExpireMinute;
    private final Integer refreshExpireDay;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public AysTokenConfiguration(AysParameterService parameterService) {

        log.info("AYS Token Configuration is initializing with AYS Parameters...");

        final Set<AysParameter> configurationParameters = parameterService.getParameters("AUTH_");

        this.issuer = AysConfigurationParameter.AYS.getDefaultValue();

        this.accessExpireMinute = Optional
                .ofNullable(AysParameter.getDefinition(AysConfigurationParameter.AUTH_ACCESS_TOKEN_EXPIRE_MINUTE, configurationParameters))
                .map(Integer::valueOf)
                .orElse(Integer.valueOf(AysConfigurationParameter.AUTH_ACCESS_TOKEN_EXPIRE_MINUTE.getDefaultValue()));

        this.refreshExpireDay = Optional
                .ofNullable(AysParameter.getDefinition(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY, configurationParameters))
                .map(Integer::valueOf)
                .orElse(Integer.valueOf(AysConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue()));

        final String privateKeyPem = Optional
                .ofNullable(AysParameter.getDefinition(AysConfigurationParameter.AUTH_TOKEN_PRIVATE_KEY, configurationParameters))
                .orElse(AysConfigurationParameter.AUTH_TOKEN_PRIVATE_KEY.getDefaultValue());
        this.privateKey = KeyConverter.convertPrivateKey(privateKeyPem);

        final String publicKeyPem = Optional
                .ofNullable(AysParameter.getDefinition(AysConfigurationParameter.AUTH_TOKEN_PUBLIC_KEY, configurationParameters))
                .orElse(AysConfigurationParameter.AUTH_TOKEN_PUBLIC_KEY.getDefaultValue());
        this.publicKey = KeyConverter.convertPublicKey(publicKeyPem);

        log.info("AYS Token Configuration is initialized!");
    }

}
