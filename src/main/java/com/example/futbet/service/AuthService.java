package com.example.futbet.service;

import com.example.futbet.dto.request.RefreshTokenRequest;
import com.example.futbet.dto.request.SignInRequest;
import com.example.futbet.dto.request.SignUpRequest;
import com.example.futbet.dto.response.AuthResponse;
import com.example.futbet.entity.User;
import com.example.futbet.enums.Role;
import com.example.futbet.exception.EmailAlreadyInUseException;
import com.example.futbet.exception.InvalidCredentialsException;
import com.example.futbet.exception.InvalidTokenException;
import com.example.futbet.mapper.UserMapper;
import com.example.futbet.repository.UserRepository;
import com.example.futbet.security.JwtService;
import com.example.futbet.security.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
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
                .avatarUrl(request.avatarUrl())
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

    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshTokenRequest request) {
        Claims claims;
        try {
            claims = jwtService.parse(request.refreshToken(), TokenType.REFRESH);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidTokenException();
        }

        UUID publicId = UUID.fromString(claims.getSubject());
        User user = userRepository.findByPublicId(publicId)
                .filter(User::isActive)
                .orElseThrow(InvalidTokenException::new);

        return buildAuthResponse(user);
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
