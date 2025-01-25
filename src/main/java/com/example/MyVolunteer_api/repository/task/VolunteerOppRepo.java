package com.example.MyVolunteer_api.repository.task;

import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerOppRepo extends JpaRepository<VolunteerOpportunities,Integer> {

    List<VolunteerOpportunities> findAllByCreatedBy(Organization organization);
    List<VolunteerOpportunities> findByTitleContainingIgnoreCase(String title);
    List<VolunteerOpportunities> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT v FROM VolunteerOpportunities v JOIN v.skills_required sr WHERE sr = :skill")
    List<VolunteerOpportunities> findBySkills_Required(@Param("skill") String skill);

    @Query("SELECT v FROM VolunteerOpportunities v WHERE CAST(v.startsAt AS string) LIKE %:searchText%")
    List<VolunteerOpportunities> findByStartsAtLike(@Param("searchText") String searchText);
}
