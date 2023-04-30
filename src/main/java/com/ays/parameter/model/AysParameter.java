package com.ays.parameter.model;

import com.ays.auth.model.enums.AysConfigurationParameter;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * A class representing a parameter used in the AYS application.
 */
@Getter
@Builder
public class AysParameter {

    private String name;
    private String definition;

    /**
     * Returns the definition of a specified configuration parameter by searching through a set of parameters.
     *
     * @param configurationParameter the configuration parameter whose definition is being sought
     * @param parameters             the set of parameters to search through
     * @return the definition of the specified configuration parameter, or null if not found
     */
    public static String getDefinition(final AysConfigurationParameter configurationParameter, final Set<AysParameter> parameters) {
        return parameters.stream()
                .filter(parameter -> parameter.getName().equals(configurationParameter.name()))
                .findFirst()
                .map(AysParameter::getDefinition)
                .orElse(null);
    }
}
