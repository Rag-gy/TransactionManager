package com.learning.tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(name = "last_name", nullable = false)
    public String lastName;

    @Column(name = "email_address", nullable = false, unique = true)
    @Email
    public String emailAddress;

    @Column(name = "archived", nullable = false)
    public Boolean archived;
}
