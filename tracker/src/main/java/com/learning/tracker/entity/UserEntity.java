package com.learning.tracker.entity;

import com.learning.tracker.enums.UserRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Builder
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
    public String emailAddress;

    @Column(name = "archived", nullable = false)
    public Boolean archived;

    @Column(name="password", nullable = false)
    public String password;

    @Column(name = "created_at")
    @CreatedDate
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    public LocalDateTime updatedAt;

    @Column(name = "role", nullable = false)
    public UserRoleEnum role;

}
