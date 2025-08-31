package com.soundaryan.iam.iam_service.service;

import com.soundaryan.iam.iam_service.dto.RegisterRequest;
import com.soundaryan.iam.iam_service.dto.UserResponse;
import com.soundaryan.iam.iam_service.exception.IAMException;
import com.soundaryan.iam.iam_service.model.Role;
import com.soundaryan.iam.iam_service.model.User;
import com.soundaryan.iam.iam_service.repository.RoleRepository;
import com.soundaryan.iam.iam_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(RegisterRequest request) {

        // --- Validate duplicates using IAMException ---
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw IAMException.duplicateUsername(request.getUsername());
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw IAMException.duplicateEmail(request.getEmail());
        }

        // --- Create new user ---
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);

        // --- Assign ROLE_USER ---
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IAMException("IAM_003", "ROLE_USER not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        // --- Prepare response ---
        Set<String> roleNames = savedUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserResponse(savedUser.getId(), savedUser.getUsername(),
                savedUser.getEmail(), roleNames);
    }
}
