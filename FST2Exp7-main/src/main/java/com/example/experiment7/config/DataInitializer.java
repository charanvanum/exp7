package com.example.experiment7.config;

import com.example.experiment7.entity.AppUser;
import com.example.experiment7.entity.Role;
import com.example.experiment7.repository.UserRepository;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("user1").isEmpty()) {
                AppUser user = new AppUser();
                user.setUsername("user1");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRoles(Set.of(Role.ROLE_USER));
                userRepository.save(user);
            }

            if (userRepository.findByUsername("admin1").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("admin1");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
                userRepository.save(admin);
            }
        };
    }
}
