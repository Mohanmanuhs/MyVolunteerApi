package com.example.MyVolunteer_api.repository;

import com.example.MyVolunteer_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
