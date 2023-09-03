package com.preduh.fakebank.repositories;

import com.preduh.fakebank.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    UserDetails findByUsername(String username);

    User findByEmail(String email);
}
