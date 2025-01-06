package com.example.MyVolunteer_api.repository.user;

import com.example.MyVolunteer_api.model.user.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<Organization, Integer> {}
