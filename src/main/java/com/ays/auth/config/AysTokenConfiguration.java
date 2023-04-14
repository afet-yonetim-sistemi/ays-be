package com.ays.auth.config;

import com.ays.auth.model.enums.AysConfigurationParameters;
import com.ays.auth.util.KeyConverter;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
@Configuration
public class AysTokenConfiguration {

    private final String issuer;
    private final Integer accessExpireMinute;
    private final Integer refreshExpireDay;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public AysTokenConfiguration() {

        this.issuer = AysConfigurationParameters.AYS.getDefaultValue();
        this.accessExpireMinute = Integer.valueOf(AysConfigurationParameters.ACCESS_TOKEN_EXPIRE_MINUTE.getDefaultValue());
        this.refreshExpireDay = Integer.valueOf(AysConfigurationParameters.REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue());
        this.privateKey = KeyConverter.convertPrivateKey(AysConfigurationParameters.AUTHENTICATION_TOKEN_PRIVATE_KEY.getDefaultValue());
        this.publicKey = KeyConverter.convertPublicKey(AysConfigurationParameters.AUTHENTICATION_TOKEN_PUBLIC_KEY.getDefaultValue());


        // TODO : this parameters should be read from database

//    public AysTokenConfiguration(AysParameterService parameterService) {
//        this.issuer = Optional
//                .ofNullable(parameterService.getValue(AysConfigurationParameters.AUTHENTICATION_TOKEN_ISSUER.name()))
//                .orElse(AysConfigurationParameters.AUTHENTICATION_TOKEN_ISSUER.getDefaultValue());
//
//        this.refreshExpireDay = Optional
//                .ofNullable(parameterService.getValue(AysConfigurationParameters.REFRESH_TOKEN_EXPIRE_DAY.name()))
//                .map(Integer::valueOf)
//                .orElse(Integer.valueOf(AysConfigurationParameters.REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue()));
//
//        this.accessExpireMinute = Optional
//                .ofNullable(parameterService.getValue(AysConfigurationParameters.ACCESS_TOKEN_EXPIRE_MINUTE.name()))
//                .map(Integer::valueOf)
//                .orElse(Integer.valueOf(AysConfigurationParameters.ACCESS_TOKEN_EXPIRE_MINUTE.getDefaultValue()));
//
//        this.privateKey = Optional
//                .ofNullable(parameterService.getValue(AysConfigurationParameters.AUTH_TOKEN_PRIVATE_KEY.name()))
//                .map(KeyConverter::convertPrivateKey)
//                .orElse(KeyConverter.convertPrivateKey(AysConfigurationParameters.AUTH_TOKEN_PRIVATE_KEY.getDefaultValue()));
//
//        this.publicKey = Optional
//                .ofNullable(parameterService.getValue(AysConfigurationParameters.AUTH_TOKEN_PUBLIC_KEY.name()))
//                .map(KeyConverter::convertPublicKey)
//                .orElse(KeyConverter.convertPublicKey(AysConfigurationParameters.AUTH_TOKEN_PUBLIC_KEY.getDefaultValue()));
    }

}
