package com.example.reidopitaco.mapper;

import com.example.reidopitaco.dto.response.UserResponse;
import com.example.reidopitaco.entity.User;
import com.example.reidopitaco.service.AvatarService;
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
