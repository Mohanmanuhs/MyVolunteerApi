package com.example.MyVolunteer_api.service.user;

import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.repository.user.VolunteerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepo volunteerRepo;

    public User createVolunteer(Volunteer volunteer) {
        return volunteerRepo.save(volunteer);
    }

    public Optional<Volunteer> findById(Integer volunteerId) {
        return volunteerRepo.findById(volunteerId);
    }
}
