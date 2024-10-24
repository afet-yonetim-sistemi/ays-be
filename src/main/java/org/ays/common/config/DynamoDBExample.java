package org.ays.common.config;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ays.common.client.AysDynamoDbClient;
import org.ays.common.model.AysDynamoDbData;
import org.ays.common.util.AysRandomUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DynamoDBExample implements CommandLineRunner {

    private final AysDynamoDbClient dynamoDbClient;

    @Override
    public void run(String... args) {

        String tableName = "AysAuditLogTest";

        AuditLog auditLog = AuditLog.builder()
                .id(AysRandomUtil.generateUUID())
                .statusCode(200)
                .createdAt(LocalDateTime.now())
                .build();

        List<AysDynamoDbData.Item> items = List.of(
                AysDynamoDbData.Item.builder()
                        .key("id")
                        .value(auditLog.getId())
                        .build(),
                AysDynamoDbData.Item.builder()
                        .key("statusCode")
                        .value(auditLog.getStatusCode())
                        .build(),
                AysDynamoDbData.Item.builder()
                        .key("createdAt")
                        .value(auditLog.getCreatedAt())
                        .build()
        );
        AysDynamoDbData data = AysDynamoDbData.builder()
                .tableName(tableName)
                .items(items)
                .build();
        dynamoDbClient.save(data);
    }

    @Getter
    @Builder
    private static class AuditLog {
        private String id;
        private Integer statusCode;
        private LocalDateTime createdAt;
    }

}