package com.example.reidopitaco.repository;

import com.example.reidopitaco.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByPublicId(UUID publicId);

    boolean existsByEmailIgnoreCase(String email);
}
