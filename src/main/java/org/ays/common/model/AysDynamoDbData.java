package org.ays.common.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class AysDynamoDbData {

    private String tableName;
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    @Getter
    @Builder
    public static class Item {
        private String key;
        private Object value;
    }

}
