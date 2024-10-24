package org.ays.common.client;

import org.ays.common.model.AysDynamoDbData;

public interface AysDynamoDbClient {

    void save(AysDynamoDbData data);

}
