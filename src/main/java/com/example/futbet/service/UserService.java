package com.example.futbet.service;

import com.example.futbet.dto.request.ChangePasswordRequest;
import com.example.futbet.dto.request.UpdateProfileRequest;
import com.example.futbet.dto.response.UserResponse;
import com.example.futbet.entity.User;
import com.example.futbet.exception.IncorrectPasswordException;
import com.example.futbet.exception.InvalidTokenException;
import com.example.futbet.mapper.UserMapper;
import com.example.futbet.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public UserResponse getMe(UUID publicId) {
        return userMapper.toResponse(loadActive(publicId));
    }

    @Transactional
    public UserResponse updateProfile(UUID publicId, UpdateProfileRequest request) {
        User user = loadActive(publicId);
        user.setName(request.name().trim());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void changePassword(UUID publicId, ChangePasswordRequest request) {
        User user = loadActive(publicId);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private User loadActive(UUID publicId) {
        return userRepository.findByPublicId(publicId)
                .filter(User::isActive)
                .orElseThrow(InvalidTokenException::new);
    }
}
