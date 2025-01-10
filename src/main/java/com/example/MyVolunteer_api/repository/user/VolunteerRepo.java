package com.example.MyVolunteer_api.repository.user;

import com.example.MyVolunteer_api.model.user.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepo extends JpaRepository<Volunteer, Integer> {
    Volunteer findByEmail(String email);
}
