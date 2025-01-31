package com.example.MyVolunteer_api.model.user;

import com.example.MyVolunteer_api.constants.Gender;
import com.example.MyVolunteer_api.constants.Role;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "userTable")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private boolean verified = false;

    @Column(unique = true)
    private String verificationToken;

    @Column
    private String phone;

    private Gender gender;

    @Column(nullable = false)
    private Role role;

}
