package com.prajwal.ecommerce_api.config;

import com.prajwal.ecommerce_api.model.Role;
import com.prajwal.ecommerce_api.model.User;
import com.prajwal.ecommerce_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class AdminSeederConfig {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);

                // 👇 Add these based on your entity's requirements
                admin.setEmail("admin@example.com");
                admin.setFirstName("Super");
                admin.setLastName("Admin");
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());

                userRepository.save(admin);
                System.out.println("✅ Admin user created: " + adminUsername);
            }
        };
    }

}
