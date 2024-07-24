package org.ays.common.model;

import org.ays.common.model.enums.AysMailTemplate;

import java.util.List;
import java.util.Map;

public class AysMailBuilder extends TestDataBuilder<AysMail> {

    public AysMailBuilder() {
        super(AysMail.class);
    }

    public AysMailBuilder withValidValues() {

        final Map<String, Object> parameters = Map.of(
                "userFullName", "Afet Yönetim Sistemi",
                "url", "http://localhost:3000/create-password/".concat("35c2d3f7-a56f-42bd-a744-51b5e11fed28")
        );

        return new AysMailBuilder()
                .withTo(List.of("test@afetyonetimsistemi.org"))
                .withTemplate(AysMailTemplate.CREATE_PASSWORD)
                .withParameters(parameters);
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
