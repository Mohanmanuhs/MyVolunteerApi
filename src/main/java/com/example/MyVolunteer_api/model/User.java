package com.example.MyVolunteer_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(nullable = false)
    private String password;

    private Gender gender;

    @Column(nullable = false)
    private Role role;

}

