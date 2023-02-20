--liquibase formatted sql

--changeset noyan:2

CREATE TABLE `roles` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `create_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `update_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `name` varchar(30) NOT NULL,
     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_ofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- rollback drop table roles