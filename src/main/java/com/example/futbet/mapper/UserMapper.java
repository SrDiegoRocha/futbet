package com.example.futbet.mapper;

import com.example.futbet.dto.response.UserResponse;
import com.example.futbet.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getPublicId(),
                user.getName(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
