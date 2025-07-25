package com.learning.tracker.utils;

import com.learning.tracker.config.properties.JwtProperties;
import com.learning.tracker.dto.users.UserDTO;
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
}
