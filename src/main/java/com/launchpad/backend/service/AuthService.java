package com.launchpad.backend.service;

import com.launchpad.backend.config.JwtUtil;
import com.launchpad.backend.dto.JwtResponse;
import com.launchpad.backend.dto.LoginRequest;
import com.launchpad.backend.dto.RegisterRequest;
import com.launchpad.backend.model.Employer;
import com.launchpad.backend.model.Student;
import com.launchpad.backend.model.User;
import com.launchpad.backend.repository.EmployerRepository;
import com.launchpad.backend.repository.StudentRepository;
import com.launchpad.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public JwtResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setName(request.getName());
        User savedUser = userRepository.save(user);

        // Create profile based on role
        if ("student".equals(request.getRole())) {
            Student student = new Student();
            student.setUserId(savedUser.getId());
            student.setEmail(request.getEmail());
            String[] nameParts = request.getName().split(" ", 2);
            student.setFirstName(nameParts[0]);
            student.setLastName(nameParts.length > 1 ? nameParts[1] : "");
            studentRepository.save(student);
        } else if ("employer".equals(request.getRole())) {
            Employer employer = new Employer();
            employer.setUserId(savedUser.getId());
            employer.setEmail(request.getEmail());
            employer.setName(request.getName());
            employerRepository.save(employer);
        }

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole());
        return new JwtResponse(token, savedUser.getRole(), savedUser.getName(), savedUser.getId());
    }

    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.getRole().equals(request.getRole())) {
            throw new RuntimeException("Invalid role for this account");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new JwtResponse(token, user.getRole(), user.getName(), user.getId());
    }
}
