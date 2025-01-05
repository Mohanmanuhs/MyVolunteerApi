package com.example.MyVolunteer_api.repository;

import com.example.MyVolunteer_api.model.user.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {}
