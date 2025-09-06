package com.learning.tracker.repository;

import com.learning.tracker.entity.UserEntity;
import com.learning.tracker.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {
    List<UserEntity> findByRole(UserRoleEnum role);
    List<UserEntity> findByArchived(Boolean archived);
    Optional<UserEntity> findByEmailAddress(String emailAddress);
    Boolean existsByEmailAddress(String emailAddress);
}
