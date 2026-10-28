package com.saurabh.crm.auth;

import com.saurabh.crm.security.JwtService;
import com.saurabh.crm.user.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager auth;
    private final JwtService jwt;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager a, JwtService j, UserRepository u, PasswordEncoder e) {
        auth = a;
        jwt = j;
        users = u;
        encoder = e;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody Login r) {
        var result = auth.authenticate(new UsernamePasswordAuthenticationToken(r.email().toLowerCase(), r.password()));
        var principal = (UserDetails) result.getPrincipal();
        var u = users.findByEmailIgnoreCase(principal.getUsername()).orElseThrow();
        return new AuthResponse(jwt.generate(principal), "Bearer", u.getEmail(), u.getFullName(), u.getRole().name());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody Register r) {
        return create(r.fullName(), r.email(), r.password(), UserRole.SALES_REP);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody AdminRegister r) {
        if (r.role() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "role is required");
        return create(r.fullName(), r.email(), r.password(), r.role());
    }

    private UserResponse create(String name, String email, String password, UserRole role) {
        if (users.existsByEmailIgnoreCase(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        AppUser u = new AppUser();
        u.setFullName(name.trim());
        u.setEmail(email.trim().toLowerCase());
        u.setPasswordHash(encoder.encode(password));
        u.setRole(role);
        u.setEnabled(true);
        u = users.save(u);
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole().name());
    }

    public record Login(@NotBlank @Email String email, @NotBlank String password) {
    }

    public record Register(@NotBlank @Size(max = 120) String fullName, @NotBlank @Email @Size(max = 180) String email,
                           @NotBlank @Size(min = 10, max = 72) String password) {
    }

    public record AdminRegister(@NotBlank @Size(max = 120) String fullName,
                                @NotBlank @Email @Size(max = 180) String email,
                                @NotBlank @Size(min = 10, max = 72) String password, UserRole role) {
    }

    public record AuthResponse(String accessToken, String tokenType, String email, String fullName, String role) {
    }

    public record UserResponse(Long id, String fullName, String email, String role) {
    }
}
