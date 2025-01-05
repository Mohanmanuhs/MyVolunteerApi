package com.example.MyVolunteer_api.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "organization")
public class Organization extends User {

    @Column(nullable = false)
    private String gstNumber;

    @Column(nullable = false)
    private String location;
}