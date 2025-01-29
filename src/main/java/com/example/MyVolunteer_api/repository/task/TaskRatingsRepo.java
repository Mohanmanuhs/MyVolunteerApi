package com.example.MyVolunteer_api.repository.task;

import com.example.MyVolunteer_api.model.task.TaskRatings;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.Volunteer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRatingsRepo extends JpaRepository<TaskRatings,Integer> {
    Optional<TaskRatings> findByTaskAndVolunteer(VolunteerOpportunities task, Volunteer volunteer);

    List<TaskRatings> findByVolunteer(Volunteer user);

    List<TaskRatings> findByOrganization(Organization user);

    @Query("SELECT v FROM Volunteer v " +
            "JOIN TaskRatings tr ON tr.volunteer = v " +
            "GROUP BY v " +
            "ORDER BY AVG(tr.ratingByOrg) DESC")
    List<Volunteer> findTop10VolunteersByRating(Pageable pageable);

    @Query("SELECT o FROM Organization o " +
            "JOIN TaskRatings tr ON tr.organization = o " +
            "GROUP BY o " +
            "ORDER BY AVG(tr.ratingByVol) DESC")
    List<Organization> findTop10OrganizationsByRating(Pageable pageable);
}
