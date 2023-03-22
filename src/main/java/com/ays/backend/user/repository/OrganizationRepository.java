package com.ays.backend.user.repository;

import com.ays.backend.user.model.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
