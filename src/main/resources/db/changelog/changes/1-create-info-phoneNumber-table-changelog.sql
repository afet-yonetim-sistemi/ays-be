--liquibase formatted sql

--changeset noyan:1

CREATE TABLE `phone_numbers` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `create_date` datetime(6) DEFAULT NULL,
     `update_date` datetime(6) DEFAULT NULL,
     `country_code` int DEFAULT NULL,
     `phone_number` int DEFAULT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- rollback drop table phone_numbers

