package org.ays.common.client.impl;

import lombok.RequiredArgsConstructor;
import org.ays.common.client.AysDynamoDbClient;
import org.ays.common.model.AysDynamoDbData;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class AysDynamoDbClientImpl implements AysDynamoDbClient {

    private final DynamoDbClient dynamoDbClient;

    @Override
    public void save(AysDynamoDbData data) {

        final Map<String, AttributeValue> items = new HashMap<>();
        for (AysDynamoDbData.Item item : data.getItems()) {

            final String key = item.getKey();
            final String value = Optional.ofNullable(item.getValue())
                    .map(Object::toString)
                    .orElse(null);

            items.put(
                    key,
                    AttributeValue.builder().s(value).build()
            );
        }

        final PutItemRequest request = PutItemRequest.builder()
                .tableName(data.getTableName())
                .item(items)
                .build();

        dynamoDbClient.putItem(request);
    }

}
