package com.example.futbet.controller;

import com.example.futbet.dto.request.ChangePasswordRequest;
import com.example.futbet.dto.request.UpdateProfileRequest;
import com.example.futbet.dto.response.UserResponse;
import com.example.futbet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal String publicId) {
        return ResponseEntity.ok(userService.getMe(UUID.fromString(publicId)));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal String publicId,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(UUID.fromString(publicId), request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal String publicId,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(UUID.fromString(publicId), request);
        return ResponseEntity.noContent().build();
    }
}
