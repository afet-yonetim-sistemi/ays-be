package org.ays.common.model;

import org.ays.common.model.enums.AysMailTemplate;

import java.util.List;
import java.util.Map;

public class AysMailBuilder extends TestDataBuilder<AysMail> {

    public AysMailBuilder() {
        super(AysMail.class);
    }

    public AysMailBuilder withValidValues() {
        return new AysMailBuilder()
                .withTo(List.of("test@afetyonetimsistemi.org"))
                .withTemplate(AysMailTemplate.EXAMPLE)
                .withParameters(Map.of("firstName", "World"));
    }

    public AysMailBuilder withTo(List<String> to) {
        data.setTo(to);
        return this;
    }

    public AysMailBuilder withTemplate(AysMailTemplate template) {
        data.setTemplate(template);
        return this;
    }

    public AysMailBuilder withParameters(Map<String, Object> parameters) {
        data.setParameters(parameters);
        return this;
    }

}
