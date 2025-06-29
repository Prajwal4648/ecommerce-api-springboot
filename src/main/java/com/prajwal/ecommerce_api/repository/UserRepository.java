package com.prajwal.ecommerce_api.repository;

import com.prajwal.ecommerce_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email); // 👈 Add this line

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
