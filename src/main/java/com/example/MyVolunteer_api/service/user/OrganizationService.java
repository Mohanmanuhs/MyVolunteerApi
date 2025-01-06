package com.example.MyVolunteer_api.service.user;

import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.repository.user.OrganizationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepo organizationRepo;

    public User createOrganization(Organization organization) {
        return organizationRepo.save(organization);
    }


}
