package dev.marcos.ticketflow_api.controller;

import dev.marcos.ticketflow_api.dto.auth.*;
import dev.marcos.ticketflow_api.dto.user.UserDetailResponse;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Create User",
            description = "Creates a new user in the system. This is a public endpoint."
    )
    public ResponseEntity<UserDetailResponse> register(@RequestBody @Valid RegisterUserRequest request) {
        UserDetailResponse user = authService.register(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.id()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticates a user and returns a JWT token. This is a public endpoint."
    )
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/google")
    @Operation(
            summary = "User Login with Google Provider",
            description = "Authenticates a user and returns a JWT token. This is a public endpoint."
    )
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody GoogleLoginRequest dto) {
        TokenResponse response = authService.loginWithGoogle(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Authenticated User", description = "Retrieve information from an authenticated user.")
    public ResponseEntity<UserDetailResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(authService.getCurrentUser(user));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Retrieve access token given a refresh token.")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
