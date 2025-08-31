package com.soundaryan.iam.iam_service.config;

import com.soundaryan.iam.iam_service.model.Role;
import com.soundaryan.iam.iam_service.model.User;
import com.soundaryan.iam.iam_service.repository.RoleRepository;
import com.soundaryan.iam.iam_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Create roles
        Role userRole = createRoleIfNotFound("ROLE_USER");
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");

        // Create default admin user if not exists
        if (userRepository.findByUsername("soundaryan").isEmpty()) {
            User admin = new User();
            admin.setUsername("soundaryan");
            admin.setEmail("soundaryanram@gmail.com");
            admin.setPassword(passwordEncoder.encode("8587@zoho")); // hashed password
            admin.setEnabled(true);

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole); // admin also has user privileges
            admin.setRoles(roles);

            userRepository.save(admin);
        }
    }

    private Role createRoleIfNotFound(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role(roleName);
            return roleRepository.save(role);
        });
    }
}
