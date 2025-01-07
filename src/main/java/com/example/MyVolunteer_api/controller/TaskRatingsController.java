package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.dto.OrganizationRatingRequest;
import com.example.MyVolunteer_api.dto.VolunteerRatingRequest;
import com.example.MyVolunteer_api.model.task.TaskRatings;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.service.task.TaskRatingsService;
import com.example.MyVolunteer_api.service.task.VolunteerOppService;
import com.example.MyVolunteer_api.service.user.OrganizationService;
import com.example.MyVolunteer_api.service.user.VolunteerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/taskRatings")
public class TaskRatingsController {


    @Autowired
    private TaskRatingsService taskRatingsService;

    @Autowired
    private VolunteerOppService volunteerOppService;


    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/{taskId}/volunteer")
    public ResponseEntity<?> submitRatingByVolunteer(
            @PathVariable Integer taskId,
            @RequestBody VolunteerRatingRequest request,
            @AuthenticationPrincipal Volunteer volunteer // Authenticated volunteer
    ) {
        try {
            VolunteerOpportunities task = volunteerOppService.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found"));

            // Check if the task belongs to the volunteer
            if (!taskRatingsService.existsByTaskAndVolunteer(task, volunteer)) {
                throw new IllegalAccessException("You are not authorized to rate this task.");
            }

            // Find or create a TaskRatings entry
            TaskRatings taskRating = taskRatingsService.findByTaskAndVolunteer(task, volunteer)
                    .orElse(new TaskRatings());

            taskRating.setTask(task);
            taskRating.setVolunteer(volunteer);
            taskRating.setOrganization(task.getCreatedBy());
            taskRating.setRatingByVol(request.getRating());
            taskRating.setFeedbackByVol(request.getFeedback());

            taskRatingsService.updateRatings(taskRating);

            return ResponseEntity.ok("Rating submitted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting rating.");
        }
    }

    @PostMapping("/{taskId}/organization")
    public ResponseEntity<?> submitRatingByOrganization(
            @PathVariable Integer taskId,
            @RequestBody OrganizationRatingRequest request,
            @AuthenticationPrincipal Organization organization // Authenticated organization
    ) {
        try {
            VolunteerOpportunities task = volunteerOppService.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found"));

            // Check if the task belongs to the organization
            if (!task.getCreatedBy().equals(organization)) {
                throw new IllegalAccessException("You are not authorized to rate this task.");
            }

            Volunteer volunteer = volunteerService.findById(request.getVolunteerId())
                    .orElseThrow(() -> new EntityNotFoundException("Volunteer not found"));

            // Find or create a TaskRatings entry
            TaskRatings taskRating = taskRatingsService.findByTaskAndVolunteer(task, volunteer)
                    .orElse(new TaskRatings());

            taskRating.setTask(task);
            taskRating.setVolunteer(volunteer);
            taskRating.setOrganization(organization);
            taskRating.setRatingByOrg(request.getRating());
            taskRating.setFeedbackByOrg(request.getFeedback());

            taskRatingsService.updateRatings(taskRating);

            return ResponseEntity.ok("Rating submitted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting rating.");
        }
    }


}
