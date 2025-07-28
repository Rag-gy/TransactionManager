package com.learning.tracker.dto.auth;

import com.learning.tracker.enums.UserRoleEnum;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Builder
public record TokenClaimDTO(
        String emailAddress,
        Long userId,
        UserRoleEnum role,
        List<String> audiences,
        String issuer,
        Date issuedAt,
        Date expiration
) {
    private static final int EXPIRATION_BUFFER_MINUTES = 30;
    public boolean isExpired() {
        long bufferMillis = EXPIRATION_BUFFER_MINUTES * 60 * 1000;
        Date expiryWithBuffer = new Date(System.currentTimeMillis() + bufferMillis);
        return expiration != null && expiration.before(expiryWithBuffer);
    }
}
