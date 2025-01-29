package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.dto.RatingRequest;
import com.example.MyVolunteer_api.model.UserPrincipal;
import com.example.MyVolunteer_api.model.task.TaskRatingId;
import com.example.MyVolunteer_api.model.task.TaskRatings;
import com.example.MyVolunteer_api.model.task.TaskSignups;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.service.task.TaskRatingsService;
import com.example.MyVolunteer_api.service.task.TaskSignupsService;
import com.example.MyVolunteer_api.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/taskRatings")
public class TaskRatingsController {

    @Autowired
    private TaskRatingsService taskRatingsService;

    @Autowired
    private TaskSignupsService taskSignupsService;

    @Autowired
    private UserService userService;



    @PostMapping("/{signUpId}/volunteer")
    public ResponseEntity<?> submitRatingByVolunteer(
            @PathVariable Integer signUpId,
            @Valid @ModelAttribute RatingRequest request
    ) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
            String email = userDetails.getUsername();

            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            TaskSignups taskSignups = taskSignupsService.findById(signUpId)
                    .orElseThrow(() -> new EntityNotFoundException("signup id not found"));

            if (user.getRole() == Role.VOLUNTEER) {
                if (taskSignups.getVolunteer() != user) {
                    throw new AuthorizationDeniedException("user is not associated with this signup");
                }

                if (taskSignups.getTask() == null) {
                    throw new EntityNotFoundException("user can't rate this task");
                }

                TaskRatings taskRating = taskRatingsService.findByTaskAndVolunteer(taskSignups.getTask(), (Volunteer) user)
                        .orElse(new TaskRatings());
                TaskRatingId taskRatingId = new TaskRatingId();
                taskRatingId.setTaskId(taskSignups.getTask().getTaskId());
                taskRatingId.setVolunteerId(user.getId());
                taskRatingId.setOrganizationId(taskSignups.getTask().getCreatedBy().getId());

                taskRating.setId(taskRatingId);
                taskRating.setTask(taskSignups.getTask());
                taskRating.setVolunteer((Volunteer) user);
                taskRating.setOrganization(taskSignups.getTask().getCreatedBy());
                taskRating.setRatingByVol(request.getRating());
                taskRating.setFeedbackByVol(request.getFeedback());
                taskRatingsService.updateRatings(taskRating);
                return ResponseEntity.ok("Rating submitted successfully.");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()+"Error submitting rating.");
        }
        return ResponseEntity.ok("some error");

    }

    @PostMapping("/{signUpId}/organization")
    public ResponseEntity<?> submitRatingByOrganization(
            @PathVariable Integer signUpId,
            @Valid @ModelAttribute RatingRequest request
    ) {
        System.out.println("feedback"+ request.getFeedback());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
            String email = userDetails.getUsername();

            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            TaskSignups taskSignups = taskSignupsService.findById(signUpId)
                    .orElseThrow(() -> new EntityNotFoundException("signup id not found"));

            if (user.getRole() == Role.ORGANIZATION) {
                if (taskSignups.getTask() == null) {
                    throw new EntityNotFoundException("organization can't rate this volunteer");
                }

                if (taskSignups.getVolunteer() == null) {
                    throw new EntityNotFoundException("organization can't rate this volunteer");
                }

                if (taskSignups.getTask().getCreatedBy() != user) {
                    throw new AuthorizationDeniedException("organization is not associated with this signup");
                }

                TaskRatings taskRating = taskRatingsService.findByTaskAndVolunteer(taskSignups.getTask(), taskSignups.getVolunteer())
                        .orElse(new TaskRatings());
                TaskRatingId taskRatingId = new TaskRatingId();
                taskRatingId.setTaskId(taskSignups.getTask().getTaskId());
                taskRatingId.setVolunteerId(taskSignups.getVolunteer().getId());
                taskRatingId.setOrganizationId(user.getId());

                taskRating.setId(taskRatingId);
                taskRating.setTask(taskSignups.getTask());
                taskRating.setVolunteer(taskSignups.getVolunteer());
                taskRating.setOrganization((Organization) user);
                taskRating.setRatingByOrg(request.getRating());
                taskRating.setFeedbackByOrg(request.getFeedback());
                taskRatingsService.updateRatings(taskRating);
                return ResponseEntity.ok("Rating submitted successfully.");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()+"Error submitting rating.");
        }
        return ResponseEntity.ok("some error");
    }

    @GetMapping("forVol")
    public ResponseEntity<?> getAllRatingsForVol() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (user.getRole() == Role.VOLUNTEER) {
            return ResponseEntity.ok(taskRatingsService.findByVolunteer((Volunteer)user,"for"));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("forOrg")
    public ResponseEntity<?> getAllRatingsForOrg() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        if (user.getRole() == Role.ORGANIZATION) {
            return ResponseEntity.ok(taskRatingsService.findByOrganization((Organization) user,"for"));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("ratings")
    public String getRatings(Model model) {
        model.addAttribute("top10Vol",taskRatingsService.top10Volunteers());
        model.addAttribute("top10Org", taskRatingsService.top10Organizations());

        return "ratingsPage";
    }

}
