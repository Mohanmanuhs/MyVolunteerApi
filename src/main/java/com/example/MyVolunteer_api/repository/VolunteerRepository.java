package com.example.MyVolunteer_api.repository;

import com.example.MyVolunteer_api.model.user.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {}
