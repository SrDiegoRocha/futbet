package com.example.futbet.mapper;

import com.example.futbet.dto.response.UserResponse;
import com.example.futbet.entity.User;
import com.example.futbet.service.AvatarService;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final AvatarService avatarService;

    public UserMapper(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getPublicId(),
                user.getName(),
                user.getEmail(),
                avatarService.avatarUrlFor(user.getName()),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
