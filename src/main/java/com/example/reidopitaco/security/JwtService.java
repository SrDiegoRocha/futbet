package com.example.reidopitaco.security;

import com.example.reidopitaco.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_NAME = "name";

    private final JwtProperties properties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.signingKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        Duration ttl = Duration.ofMinutes(properties.accessTokenExpirationMinutes());
        return buildToken(user, TokenType.ACCESS, ttl);
    }

    public String generateRefreshToken(User user) {
        Duration ttl = Duration.ofDays(properties.refreshTokenExpirationDays());
        return buildToken(user, TokenType.REFRESH, ttl);
    }

    public long getAccessTokenExpirationSeconds() {
        return Duration.ofMinutes(properties.accessTokenExpirationMinutes()).toSeconds();
    }

    public Claims parse(String token, TokenType expectedType) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(properties.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String actualType = claims.get(CLAIM_TYPE, String.class);
        if (actualType == null || !actualType.equals(expectedType.name())) {
            throw new IllegalArgumentException("Invalid token type");
        }
        return claims;
    }

    private String buildToken(User user, TokenType type, Duration ttl) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(ttl);

        return Jwts.builder()
                .issuer(properties.issuer())
                .id(UUID.randomUUID().toString())   // jti — permite revogação (denylist) de refresh tokens
                .subject(user.getPublicId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .claim(CLAIM_TYPE, type.name())
                .claim(CLAIM_ROLE, user.getRole().name())
                .claim(CLAIM_NAME, user.getName())
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }
}
