--liquibase formatted sql

--changeset noyan:4

CREATE TABLE `types` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `create_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `update_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `name` varchar(20) DEFAULT NULL,
     `user_id` bigint DEFAULT NULL,
     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_17go525ou3scbmd4pcftq130f` (`name`),
     KEY `FK6dyj8uw42lrvggspwsaufal4c` (`user_id`),
     CONSTRAINT `FK6dyj8uw42lrvggspwsaufal4c` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- rollback drop table type