package com.example.MyVolunteer_api.repository.user;

import com.example.MyVolunteer_api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByEmail(String email);

}
