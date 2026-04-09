package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(UserService userService, PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
    System.out.println("=== LOGIN ATTEMPT ===");
    System.out.println("Email: " + loginRequestDTO.getEmail());

    Optional<User> user = userService.findByEmail(loginRequestDTO.getEmail());
    System.out.println("User found: " + user.isPresent());

    if (user.isPresent()) {
      System.out.println("Stored hash: " + user.get().getPassword());
      boolean matches = passwordEncoder.matches(loginRequestDTO.getPassword(),
          user.get().getPassword());
      System.out.println("Password matches: " + matches);
    }

    return user
        .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),
            u.getPassword()))
        .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
  }

  public boolean validateToken(String token) {
    try {
      jwtUtil.validateToken(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }
}