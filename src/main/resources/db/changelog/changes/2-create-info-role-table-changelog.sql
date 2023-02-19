--liquibase formatted sql

--changeset noyan:1

CREATE TABLE `roles` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `create_date` datetime(6) DEFAULT NULL,
     `update_date` datetime(6) DEFAULT NULL,
     `name` varchar(30) DEFAULT NULL,
     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_ofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- rollback drop table roles