package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.entity.RefreshToken;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.exception.BusinessException;
import dev.marcos.ticketflow_api.exception.NotFoundException;
import dev.marcos.ticketflow_api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${spring.security.jwt.refresh-token.expiration-seconds}")
    private Long refreshTokenDurationSeconds;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Transactional
    public RefreshToken createRefreshToken(UUID userId) {

        User user = userService.findById(userId);

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationSeconds));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Refresh token não encontrado"));
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new BusinessException("Refresh token expirado. Faça login novamente");
        }
    }

    @Transactional
    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }
}
