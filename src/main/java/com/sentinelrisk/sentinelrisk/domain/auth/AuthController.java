package com.sentinelrisk.sentinelrisk.domain.auth;

import com.sentinelrisk.sentinelrisk.domain.user.UserRepository;
import com.sentinelrisk.sentinelrisk.domain.user.Users;
import com.sentinelrisk.sentinelrisk.infrastructure.security.JwtService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El usuario ya existe");
        }

        Users user = new Users();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("ROLE_USER");

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Usuario creado con éxito"));
    }


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> loginSwagger(@RequestParam String username, @RequestParam String password) {
        return performLogin(username, password);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginJson(@RequestBody AuthRequest request) {
        return performLogin(request.username(), request.password());
    }

    private ResponseEntity<?> performLogin(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        Users user = userRepository.findByUsername(username)
                .orElseThrow();

        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "access_token", token,
                "token_type", "Bearer"
        ));
    }
}