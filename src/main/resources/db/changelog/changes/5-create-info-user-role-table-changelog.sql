
--liquibase formatted sql

--changeset noyan:5

CREATE TABLE `user_roles` (
      `user_id` bigint NOT NULL,
      `role_id` bigint NOT NULL,
      PRIMARY KEY (`user_id`,`role_id`),
      KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
      CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
      CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- rollback drop table user_roles