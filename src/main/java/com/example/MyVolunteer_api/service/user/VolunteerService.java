package com.example.MyVolunteer_api.service.user;

import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.repository.user.VolunteerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepo volunteerRepo;

    public User createVolunteer(Volunteer volunteer) {
        return volunteerRepo.save(volunteer);
    }


}
