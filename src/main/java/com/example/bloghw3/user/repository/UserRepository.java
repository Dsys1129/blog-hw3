package com.example.bloghw3.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloghw3.user.entity.User;
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
