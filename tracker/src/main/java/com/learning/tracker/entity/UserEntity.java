package com.learning.tracker.entity;

import com.learning.tracker.enums.UserRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(name = "last_name", nullable = false)
    public String lastName;

    @Column(name = "email_address", nullable = false, unique = true)
    @Email
    public String username;

    @Column(name = "archived", nullable = false)
    public Boolean archived;

    @Column(name="password", nullable = false)
    public String password;

    @Column(name = "created_at")
    public Instant createdAt;

    @Column(name = "role", nullable = false)
    public UserRoleEnum role;

}
