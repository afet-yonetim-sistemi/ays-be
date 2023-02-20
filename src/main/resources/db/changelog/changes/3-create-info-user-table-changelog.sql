
--liquibase formatted sql

--changeset noyan:3

CREATE TABLE `users` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `create_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `update_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     `latitude` double NOT NULL,
     `longitude` double NOT NULL,
     `password` varchar(255) NOT NULL,
     `status` varchar(20) NOT NULL,
     `useruuid` binary(16) NOT NULL,
     `username` varchar(255) NOT NULL,
     `phone_number_id` bigint DEFAULT NULL,
     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_ain6rnqaxwdkd2y7039apay37` (`status`),
     UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
     KEY `FKsm22r0ln4w8wg55nd1iyddkpv` (`phone_number_id`),
     CONSTRAINT `FKsm22r0ln4w8wg55nd1iyddkpv` FOREIGN KEY (`phone_number_id`) REFERENCES `phone_numbers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- rollback drop table users