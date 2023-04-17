package com.ays.parameter.model;

import com.ays.auth.model.enums.AysConfigurationParameter;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class AysParameter {

    private String name;
    private String definition;

    public static String getDefinition(final AysConfigurationParameter configurationParameter, final Set<AysParameter> parameters) {
        return parameters.stream()
                .filter(parameter -> parameter.getName().equals(configurationParameter.name()))
                .findFirst()
                .map(AysParameter::getDefinition)
                .orElse(null);
    }
}
