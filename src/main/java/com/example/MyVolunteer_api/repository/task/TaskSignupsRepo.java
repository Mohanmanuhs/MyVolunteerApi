package com.example.MyVolunteer_api.repository.task;

import com.example.MyVolunteer_api.constants.SignUpStatus;
import com.example.MyVolunteer_api.model.task.TaskSignups;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Volunteer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskSignupsRepo extends JpaRepository<TaskSignups,Integer> {

    List<TaskSignups> findByVolunteer(Volunteer volunteer);

    @Query("SELECT ts.task FROM TaskSignups ts WHERE ts.volunteer = :volunteer AND ts.status = :status")
    List<VolunteerOpportunities> findTasksByVolunteer(@Param("volunteer") Volunteer volunteer, @Param("status") SignUpStatus status);

    List<TaskSignups> findByTask(VolunteerOpportunities task);



    @Query("SELECT COUNT(ts) > 0 FROM TaskSignups ts WHERE ts.volunteer = :volunteer AND ts.task = :task AND ts.status = :status")
    boolean existsByVolunteerAndTaskAndStatus(@Param("volunteer") Volunteer volunteer,
                                              @Param("task") VolunteerOpportunities task,
                                              @Param("status") SignUpStatus status);

    @Query("SELECT ts FROM TaskSignups ts WHERE ts.volunteer = :volunteer AND ts.task = :task")
    Optional<TaskSignups> getSignupByVolunteerAndTask(@Param("volunteer") Volunteer volunteer,
                                                      @Param("task") VolunteerOpportunities task);

    @Modifying
    @Transactional
    @Query("UPDATE TaskSignups ts SET ts.status = :status WHERE ts.volunteer = :volunteer AND ts.task = :task")
    int cancelSignUpByVolunteerAndTask(@Param("status") SignUpStatus status,
                                       @Param("volunteer") Volunteer volunteer,
                                       @Param("task") VolunteerOpportunities task);

}
