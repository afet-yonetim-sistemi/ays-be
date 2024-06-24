package org.ays.parameter.model;

import org.ays.common.model.TestDataBuilder;

public class AysParameterBuilder extends TestDataBuilder<AysParameter> {

    public AysParameterBuilder() {
        super(AysParameter.class);
    }

    public AysParameterBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysParameterBuilder withDefinition(String definition) {
        data.setDefinition(definition);
        return this;
    }

}
