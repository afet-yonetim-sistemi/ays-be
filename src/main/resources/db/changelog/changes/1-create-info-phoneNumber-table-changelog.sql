--liquibase formatted sql

--changeset noyan:1

CREATE TABLE `phone_numbers` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `create_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `update_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `country_code` int NOT NULL,
     `number_of_phone` int NOT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- rollback drop table phone_numbers

