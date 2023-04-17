package com.ays.parameter.service;

import com.ays.parameter.model.AysParameter;

import java.util.Set;

public interface AysParameterService {

    Set<AysParameter> getParameters(String prefixOfName);

}
