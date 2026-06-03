package com.example.reidopitaco.service;

import com.example.reidopitaco.dto.request.RefreshTokenRequest;
import com.example.reidopitaco.dto.request.SignInRequest;
import com.example.reidopitaco.dto.request.SignUpRequest;
import com.example.reidopitaco.dto.response.AuthResponse;
import com.example.reidopitaco.entity.RevokedToken;
import com.example.reidopitaco.entity.User;
import com.example.reidopitaco.enums.Role;
import com.example.reidopitaco.exception.EmailAlreadyInUseException;
import com.example.reidopitaco.exception.InvalidCredentialsException;
import com.example.reidopitaco.exception.InvalidTokenException;
import com.example.reidopitaco.mapper.UserMapper;
import com.example.reidopitaco.repository.RevokedTokenRepository;
import com.example.reidopitaco.repository.UserRepository;
import com.example.reidopitaco.security.JwtService;
import com.example.reidopitaco.security.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthService(
            UserRepository userRepository,
            RevokedTokenRepository revokedTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.revokedTokenRepository = revokedTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new EmailAlreadyInUseException();
        }

        User user = User.builder()
                .name(request.name().trim())
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);
        return buildAuthResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.isActive()) {
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        Claims claims;
        try {
            claims = jwtService.parse(request.refreshToken(), TokenType.REFRESH);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidTokenException();
        }

        if (isRevoked(claims)) {
            throw new InvalidTokenException();
        }

        UUID publicId = UUID.fromString(claims.getSubject());
        User user = userRepository.findByPublicId(publicId)
                .filter(User::isActive)
                .orElseThrow(InvalidTokenException::new);

        // Rotação: o refresh token apresentado é revogado e um novo par é emitido. Impede o
        // reuso de um token antigo (inclusive vazado) depois que ele já foi trocado.
        revoke(claims);

        return buildAuthResponse(user);
    }

    /**
     * Logout: revoga o refresh token informado (entra na denylist). Lenient — se o token for
     * inválido/expirado, não há o que revogar e a operação é tratada como sucesso (idempotente).
     */
    @Transactional
    public void logout(RefreshTokenRequest request) {
        Claims claims;
        try {
            claims = jwtService.parse(request.refreshToken(), TokenType.REFRESH);
        } catch (JwtException | IllegalArgumentException ex) {
            return;
        }
        revoke(claims);
    }

    private boolean isRevoked(Claims claims) {
        String jti = claims.getId();
        return jti != null && revokedTokenRepository.existsByJti(UUID.fromString(jti));
    }

    private void revoke(Claims claims) {
        String jti = claims.getId();
        if (jti == null) {
            return; // tokens antigos (pré-jti) não são revogáveis; expiram naturalmente
        }
        UUID jtiUuid = UUID.fromString(jti);
        if (revokedTokenRepository.existsByJti(jtiUuid)) {
            return;
        }
        UUID userPublicId = claims.getSubject() != null ? UUID.fromString(claims.getSubject()) : null;
        Instant expiresAt = claims.getExpiration() != null
                ? claims.getExpiration().toInstant()
                : Instant.now();
        revokedTokenRepository.save(
                RevokedToken.builder()
                        .jti(jtiUuid)
                        .userPublicId(userPublicId)
                        .expiresAt(expiresAt)
                        .build()
        );
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.of(
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpirationSeconds(),
                userMapper.toResponse(user)
        );
    }
}
