package com.learning.tracker.specification;

import com.learning.tracker.entity.UserEntity;
import com.learning.tracker.enums.UserRoleEnum;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<UserEntity> hasRole(UserRoleEnum role) {
        return (root, query, criteriaBuilder) ->
                role == null ? null : criteriaBuilder.equal(root.get("role"), role);
    }

    public static Specification<UserEntity> hasArchived(Boolean archived) {
        return (root, query, criteriaBuilder) ->
                archived == null ? null : criteriaBuilder.equal(root.get("archived"), archived);
    }

    public static Specification<UserEntity> hasEmailAddress(String emailAddress) {
        return (root, query, criteriaBuilder) ->
                emailAddress == null ? null : criteriaBuilder.equal(root.get("emailAddress"), emailAddress);
    }

    public static Specification<UserEntity> hasSearchTerm(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return null;
            }
            String likePattern = "%" + searchTerm.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("emailAddress")), likePattern)
            );
        };
    }

    public static Specification<UserEntity> withFilters(
            UserRoleEnum role,
            Boolean archived,
            String emailAddress,
            String searchTerm
    ) {
        Specification<UserEntity> spec = Specification.allOf();
        if(role != null) {
            spec = spec.and(hasRole(role));
        }
        if(archived != null) {
            spec = spec.and(hasArchived(archived));
        }
        if(emailAddress != null) {
            spec = spec.and(hasEmailAddress(emailAddress));
        }
        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and(hasSearchTerm(searchTerm));
        }
        return spec;
    }
}
