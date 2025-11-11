package com.ajemi.backend.config;

import java.util.Set;

import com.ajemi.backend.entity.Role;
import com.ajemi.backend.entity.Role.RoleName;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.RoleRepository;
import com.ajemi.backend.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 🧩 1. نزيدو الأدوار إذا ما كايناش
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setName(RoleName.USER);
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName(RoleName.ADMIN);
            roleRepository.save(adminRole);

            System.out.println("✅ Roles created: USER, ADMIN");
        }

        // 🧍‍♂️ 2. نزيدو admin إذا ما كاينش
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // mot de passe crypté
            admin.setRole(roleRepository.findByName(RoleName.ADMIN).get());

            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin@example.com / admin123");
        }
    }
}
