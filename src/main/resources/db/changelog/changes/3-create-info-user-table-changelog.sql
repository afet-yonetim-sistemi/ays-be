
--liquibase formatted sql

--changeset noyan:1

CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `create_date` datetime(6) DEFAULT NULL,
                         `update_date` datetime(6) DEFAULT NULL,
                         `latitude` double DEFAULT NULL,
                         `longitude` double DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `status` varchar(20) DEFAULT NULL,
                         `useruuid` binary(16) DEFAULT NULL,
                         `username` varchar(255) DEFAULT NULL,
                         `user_id` bigint DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_ain6rnqaxwdkd2y7039apay37` (`status`),
                         KEY `FKa5v4s3unl80sseswa1swa6cjt` (`user_id`),
                         CONSTRAINT `FKa5v4s3unl80sseswa1swa6cjt` FOREIGN KEY (`user_id`) REFERENCES `phone_numbers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- rollback drop table users