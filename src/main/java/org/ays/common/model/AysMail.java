package org.ays.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.enums.AysMailTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class AysMail {

    private List<String> to;
    private AysMailTemplate template;
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();

}
