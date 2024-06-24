package org.ays.parameter.model.entity;

import org.ays.common.model.TestDataBuilder;

public class AysParameterEntityBuilder extends TestDataBuilder<AysParameterEntity> {

    public AysParameterEntityBuilder() {
        super(AysParameterEntity.class);
    }

    public AysParameterEntityBuilder withName(String name) {
        data.setName(name);
        return this;
    }

    public AysParameterEntityBuilder withDefinition(String definition) {
        data.setDefinition(definition);
        return this;
    }

}