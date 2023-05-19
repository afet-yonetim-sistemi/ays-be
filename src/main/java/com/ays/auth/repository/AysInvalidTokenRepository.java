package com.ays.auth.repository;

import com.ays.auth.model.entity.AysInvalidTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AysInvalidTokenRepository extends JpaRepository<AysInvalidTokenEntity, Long> {

    Optional<AysInvalidTokenEntity> findByTokenId(String tokenId);

}
