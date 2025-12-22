package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.auth.*;
import dev.marcos.ticketflow_api.dto.user.UserDetailResponse;
import dev.marcos.ticketflow_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDetailResponse> register(@RequestBody @Valid RegisterUserRequest request) {
        UserDetailResponse user = authService.register(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.id()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody GoogleLoginRequest dto) {
        TokenResponse response = authService.loginWithGoogle(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailResponse> me() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
