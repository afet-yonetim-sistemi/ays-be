--liquibase formatted sql

--changeset noyan:1

CREATE TABLE `type` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `create_date` datetime(6) DEFAULT NULL,
    `update_date` datetime(6) DEFAULT NULL,
    `name` varchar(20) DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_3tg65hx29l2ser69ddfwvhy4h` (`name`),
    KEY `FK4bms0cp6jv2v5k9c0yaxg4g5t` (`user_id`),
    CONSTRAINT `FK4bms0cp6jv2v5k9c0yaxg4g5t` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- rollback drop table type