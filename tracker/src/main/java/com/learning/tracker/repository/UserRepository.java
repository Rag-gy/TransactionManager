package com.learning.tracker.repository;

import com.learning.tracker.entity.UserEntity;
import com.learning.tracker.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByRole(UserRoleEnum role);
    List<UserEntity> findByArchived(Boolean archived);
    Boolean existsByUsername(String username);
    UserEntity findByEmailAddress(String emailAddress);
}
