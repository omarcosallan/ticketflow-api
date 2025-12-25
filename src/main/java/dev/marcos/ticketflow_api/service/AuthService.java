package dev.marcos.ticketflow_api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import dev.marcos.ticketflow_api.dto.auth.*;
import dev.marcos.ticketflow_api.dto.user.UserDetailResponse;
import dev.marcos.ticketflow_api.entity.RefreshToken;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.exception.BusinessException;
import dev.marcos.ticketflow_api.mapper.UserMapper;
import dev.marcos.ticketflow_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    public UserDetailResponse register(RegisterUserRequest dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Este email já está em uso");
        }

        User newUser = new User();
        newUser.setName(dto.name());
        newUser.setEmail(dto.email());
        newUser.setPasswordHash(passwordEncoder.encode(dto.password()));
        newUser.setIsSystemAdmin(false);
        newUser.setEmailVerified(false);
        newUser.setProvider("LOCAL");

        User savedUser = userRepository.save(newUser);

        return userMapper.toUserDetailDTO(savedUser);
    }

    public TokenResponse login(LoginRequest dto) {
        UsernamePasswordAuthenticationToken usernamePassword  = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        User user = (User) Objects.requireNonNull(auth.getPrincipal());

        String token = tokenService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new TokenResponse(token, refreshToken.getToken());
    }

    public TokenResponse loginWithGoogle(GoogleLoginRequest dto) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(dto.token());

            if (idToken == null) {
                throw new BusinessException("Token do Google inválido ou expirado");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleId = payload.getSubject();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            boolean emailVerified = (Boolean) payload.get("email_verified");

            User user = userRepository.findByEmail(email)
                    .map(existingUser -> updateExistingUser(existingUser, googleId, pictureUrl, emailVerified))
                    .orElseGet(() -> createGoogleUser(email, name, googleId, pictureUrl, emailVerified));


            String jwt = tokenService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

            return new TokenResponse(jwt, refreshToken.getToken());

        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public TokenResponse refreshToken(RefreshTokenRequest dto) {
        RefreshToken oldToken = refreshTokenService.findByToken(dto.refreshToken());
        refreshTokenService.verifyExpiration(oldToken);

        User user = oldToken.getUser();

        refreshTokenService.delete(oldToken);

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
        String newAccessToken = tokenService.generateToken(user);

        return new TokenResponse(newAccessToken, newRefreshToken.getToken());
    }

    public UserDetailResponse getCurrentUser(User user) {
        return userMapper.toUserDetailDTO(user);
    }

    private User updateExistingUser(User user, String googleId, String avatarUrl, boolean emailVerified) {
        if (user.getGoogleId() == null) {
            user.setGoogleId(googleId);
            user.setAvatarUrl(avatarUrl);
            user.setProvider("GOOGLE");
            user.setEmailVerified(emailVerified);
            userRepository.save(user);
        }
        return user;
    }

    private User createGoogleUser(String email, String name, String googleId, String avatarUrl, boolean emailVerified) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setGoogleId(googleId);
        newUser.setAvatarUrl(avatarUrl);
        newUser.setProvider("GOOGLE");
        newUser.setEmailVerified(emailVerified);

        return userRepository.save(newUser);
    }
}
