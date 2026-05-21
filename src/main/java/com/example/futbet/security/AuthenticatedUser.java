package com.example.futbet.security;

import com.example.futbet.entity.User;
import com.example.futbet.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AuthenticatedUser implements UserDetails {

    private final UUID publicId;
    private final String email;
    private final String password;
    private final Role role;
    private final boolean active;

    public AuthenticatedUser(User user) {
        this.publicId = user.getPublicId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.active = user.isActive();
    }

    public UUID getPublicId() {
        return publicId;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
