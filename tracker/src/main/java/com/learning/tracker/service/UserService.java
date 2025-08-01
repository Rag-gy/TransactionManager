package com.learning.tracker.service;

import com.learning.tracker.config.CustomUserPrincipal;
import com.learning.tracker.dto.users.RegisterUserDTO;
import com.learning.tracker.dto.users.UserDTO;
import com.learning.tracker.dto.users.UserProfileUpdateRequestDTO;
import com.learning.tracker.entity.UserEntity;
import com.learning.tracker.enums.UserRoleEnum;
import com.learning.tracker.exception.AuthorisationException;
import com.learning.tracker.exception.ResourceNotFoundException;
import com.learning.tracker.exception.ValidationException;
import com.learning.tracker.repository.UserRepository;
import com.learning.tracker.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) {
        log.debug("Loading user by email for authentication: {}", emailAddress);

        UserEntity user = userRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new ResourceNotFoundException("User account is not not found"));

        if (user.archived) {
            throw new UsernameNotFoundException("User account is deactivated");
        }

        return new CustomUserPrincipal(user);
    }

    private UserEntity getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()){
            throw new ValidationException("User not authenticated");
        }

        if (!(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {
            throw new ValidationException("Invalid authentication principal");
        }

        return principal.getUserEntity();
    }

    @Transactional
    public Long createUser(RegisterUserDTO userRequest) {
        log.debug("Creating user with email: {}", userRequest.emailAddress());

        UserEntity currentUser = getCurrentAuthenticatedUser();

        if (userRequest.role() == UserRoleEnum.ADMIN && currentUser.role != UserRoleEnum.ADMIN) {
            log.warn("User with email {} attempted to create an admin user without permission",
                    currentUser.emailAddress);
            throw new AuthorisationException("Only admin users can create other admin users");
        }

        if (userRepository.existsByEmailAddress(userRequest.emailAddress())) {
            log.warn("User with email {} already exists", userRequest.emailAddress());
            throw new ValidationException("User with this email address already exists");
        }

        UserEntity user = UserEntity.builder()
                .emailAddress(userRequest.emailAddress())
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .password(passwordEncoder.encode(userRequest.password()))
                .role(userRequest.role())
                .archived(false)
                .build();
        UserEntity savedUser = userRepository.save(user);
        return savedUser.id;
    }

    @Transactional
    public Long updateUser(Long userId, UserProfileUpdateRequestDTO request) {
        UserEntity currentUser = getCurrentAuthenticatedUser();
        if(currentUser.role != UserRoleEnum.ADMIN && request.role() != null && request.role() == UserRoleEnum.ADMIN) {
            log.warn("User with email {} attempted to update a user to admin role without permission",
                    currentUser.emailAddress);
            throw new AuthorisationException("Only admin users can update other users to admin role");
        }
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User does not exist"));

        if(request.firstName() != null) {
           user.firstName = request.firstName();
        }
        if(request.lastName() != null) {
            user.lastName = request.lastName();
        }
        UserEntity updatedUser = userRepository.save(user);
        log.info("User with id {} updated successfully", updatedUser.id);
        return updatedUser.id;
    }

    @Transactional
    public List<UserDTO> getAllUsers(
            String searchTerm, String emailAddress, UserRoleEnum role, Boolean archived
    ) {
        log.debug("Fetching all users with searchTerm: {}, emailAddress: {}, role: {}, archived: {}",
                searchTerm, emailAddress, role, archived);

        Specification<UserEntity> spec = UserSpecification.withFilters(role, archived, emailAddress, searchTerm);
        List<UserEntity> allUserEntity = userRepository.findAll(spec);
        List<UserDTO> userDTOs = allUserEntity.stream()
                .map(UserDTO::fromEntity)
                .toList();
        log.debug("Found {} users matching the criteria", userDTOs.size());
        return userDTOs;
    }

    @Transactional
    public UserDTO getUserById(Long userId) {
        log.debug("Fetching user by id: {}", userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return UserDTO.fromEntity(user);
    }

    @Transactional
    public Long resetPassword(String emailAddress, String newPassword) {
        UserEntity currentUser = getCurrentAuthenticatedUser();
        UserEntity userToUpdate = userRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        if (!currentUser.id.equals(userToUpdate.id)){
            if(currentUser.role == UserRoleEnum.ADMIN){
                log.debug("Admin user {} is resetting password for user with id {}",
                        currentUser.emailAddress, userToUpdate.id);
                userToUpdate.password = passwordEncoder.encode(newPassword);
                UserEntity updatedUser = userRepository.save(userToUpdate);
                return updatedUser.id;
            }
            log.warn("User with email {} attempted to reset password for user with id {} without permission",
                    currentUser.emailAddress, userToUpdate.id);
            throw new AuthorisationException("Only the user themselves or an admin can change the password");
        }

        userToUpdate.password = passwordEncoder.encode(newPassword);
        UserEntity updatedUser = userRepository.save(userToUpdate);
        log.info("Password reset successfully for user with id {}", updatedUser.id);
        return updatedUser.id;
    }

    public Long archiveUser(Long userId, boolean archiveValue) {
        UserEntity currentUser = getCurrentAuthenticatedUser();
        UserEntity userToArchive = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        if (!Objects.equals(userToArchive.id, userId)) {
            if (currentUser.role == UserRoleEnum.ADMIN) {
                log.debug("Admin user {} is archiving user with id {}", currentUser.emailAddress, userId);
                userToArchive.archived = archiveValue;
                UserEntity updatedUser = userRepository.save(userToArchive);
                return updatedUser.id;
            }
            log.warn("User with email {} attempted to archive / un archive user with id {} without permission",
                    currentUser.emailAddress, userId);
        }
        userToArchive.archived = archiveValue;
        UserEntity updatedUser = userRepository.save(userToArchive);
        return updatedUser.id;
    }

}
