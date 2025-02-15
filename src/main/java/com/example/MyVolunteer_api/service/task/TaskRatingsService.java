package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.dto.task.VolRatingsDto;
import com.example.MyVolunteer_api.model.task.TaskRatings;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.repository.task.TaskRatingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskRatingsService {

    @Autowired
    private TaskRatingsRepo taskRatingsRepo;


    public Optional<TaskRatings> findByTaskAndVolunteer(VolunteerOpportunities task, Volunteer volunteer) {
        return taskRatingsRepo.findByTaskAndVolunteer(task, volunteer);
    }

    public List<String> top10Volunteers() {
        return taskRatingsRepo.findTop10VolunteersByRating(PageRequest.of(0, 10)).stream().map(User::getName).collect(Collectors.toList());
    }

    public List<String> top10Organizations() {
        return taskRatingsRepo.findTop10OrganizationsByRating(PageRequest.of(0, 10)).stream().map(User::getName).collect(Collectors.toList());
    }

    public void updateRatings(TaskRatings taskRating) {
        taskRatingsRepo.save(taskRating);
    }

    public List<VolRatingsDto> findByVolunteer(Volunteer user, String filter) {
        if (Objects.equals(filter, "for")) {
            return taskRatingsRepo.findByVolunteer(user).stream().filter(taskRatings -> taskRatings.getRatingByOrg() != null).map(taskRatings -> new VolRatingsDto(taskRatings.getRatingByOrg(), taskRatings.getOrganization().getName(), taskRatings.getFeedbackByOrg())).collect(Collectors.toList());
        } else {
            return taskRatingsRepo.findByVolunteer(user).stream().filter(taskRatings -> taskRatings.getRatingByVol() != null).map(taskRatings -> new VolRatingsDto(taskRatings.getRatingByVol(), taskRatings.getOrganization().getName(), taskRatings.getFeedbackByVol())).collect(Collectors.toList());
        }
    }

    public List<VolRatingsDto> findByOrganization(Organization user, String filter) {
        if (Objects.equals(filter, "for")) {
            return taskRatingsRepo.findByOrganization(user).stream().filter(taskRatings -> taskRatings.getRatingByVol() != null).map(taskRatings -> new VolRatingsDto(taskRatings.getRatingByVol(), taskRatings.getVolunteer().getName(), taskRatings.getFeedbackByVol())).collect(Collectors.toList());
        } else {
            return taskRatingsRepo.findByOrganization(user).stream().filter(taskRatings -> taskRatings.getRatingByOrg() != null).map(taskRatings -> new VolRatingsDto(taskRatings.getRatingByOrg(), taskRatings.getVolunteer().getName(), taskRatings.getFeedbackByOrg())).collect(Collectors.toList());
        }
    }
}
