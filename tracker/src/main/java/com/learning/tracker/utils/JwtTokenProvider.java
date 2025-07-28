package com.learning.tracker.utils;

import com.learning.tracker.config.properties.JwtProperties;
import com.learning.tracker.dto.auth.TokenClaimDTO;
import com.learning.tracker.dto.users.UserDTO;
import com.learning.tracker.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenProvider {
    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private static final String USER_ID_CLAIM = "userId";
    private static final String EMAIL_CLAIM = "emailAddress";
    private static final String ROLE_CLAIM = "role";
    private static final String TOKEN_ID_CLAIM = "jti";
    private static final String AUDIENCE_CLAIM = "aud";

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes());
    }

    public String generateAccessToken(UserDTO user, List<String> audiences) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(jwtProperties.accessTokenValidityInSeconds(), ChronoUnit.SECONDS);

        JwtBuilder accessTokenBuilder =  Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(user.emailAddress())
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                .claim(TOKEN_ID_CLAIM, UUID.randomUUID().toString())
                .claim(USER_ID_CLAIM, user.id())
                .claim(EMAIL_CLAIM, user.emailAddress())
                .claim(ROLE_CLAIM, user.role().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryDate));

        if (audiences != null && !audiences.isEmpty()) {
            accessTokenBuilder.claim(AUDIENCE_CLAIM, audiences);
        }
        return accessTokenBuilder.signWith(signingKey).compact();
    }

    public String generateRefreshToken(UserDTO user, List<String> audiences) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(jwtProperties.refreshTokenValidityInSeconds(), ChronoUnit.SECONDS);

        JwtBuilder refreshTokenBuilder = Jwts.builder()
                .subject(user.emailAddress())
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                .claim(USER_ID_CLAIM, user.id())
                .issuer(jwtProperties.issuer())
                .claim(TOKEN_ID_CLAIM, UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryDate));

        if (audiences != null && !audiences.isEmpty()) {
            refreshTokenBuilder.claim(AUDIENCE_CLAIM, audiences);
        }
        return refreshTokenBuilder.signWith(signingKey).compact();
    }

    private Optional<Claims> extractAllClaims(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
            );
        }
        catch ( Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private <T> Optional<T> extractClaim(String token, Function<Claims, T> claimResolver) {
        return extractAllClaims(token).map(claimResolver);
    }

    public boolean validateAccessToken(String token) {
        if(token != null && !token.isBlank()) {
            return extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class))
                    .map(ACCESS_TOKEN_TYPE::equals)
                    .orElse(false);
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        if(token != null && !token.isBlank()) {
            return extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class))
                    .map(REFRESH_TOKEN_TYPE::equals)
                    .orElse(false);
        }
        return false;
    }

    public Optional<TokenClaimDTO> extractAllUserData(String token) {
        try {
            Optional<Claims> claimsOpt = extractAllClaims(token);
            if(claimsOpt.isEmpty()) {
                return Optional.empty();
            }
            Claims claims = claimsOpt.get();

            return Optional.of(
                    TokenClaimDTO.builder()
                            .userId(claims.get(USER_ID_CLAIM, Long.class))
                            .emailAddress(claims.get(EMAIL_CLAIM, String.class))
                            .role(UserRoleEnum.valueOf(claims.get(ROLE_CLAIM, String.class)))
                            .audiences(claims.get(AUDIENCE_CLAIM, List.class))
                            .issuedAt(claims.getIssuedAt())
                            .issuer(claims.getIssuer())
                            .expiration(claims.getExpiration())
                            .build()
            );
        } catch (Exception e) {
            log.warn("Failed to extract user data from token: {}", e.getMessage());
            return Optional.empty();
        }
    }

}
