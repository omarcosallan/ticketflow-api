package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.dto.auth.AuthResponseDTO;
import dev.marcos.ticketflow_api.dto.auth.LoginRequestDTO;
import dev.marcos.ticketflow_api.dto.auth.RegisterRequestDTO;
import dev.marcos.ticketflow_api.dto.auth.UserResponseDTO;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.exception.BusinessException;
import dev.marcos.ticketflow_api.mapper.UserMapper;
import dev.marcos.ticketflow_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public UserResponseDTO register(RegisterRequestDTO dto) {

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

        return userMapper.toDTO(savedUser);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        UsernamePasswordAuthenticationToken usernamePassword  = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        User user = (User) Objects.requireNonNull(auth.getPrincipal());
        String token = tokenService.generateToken(user);
        return new AuthResponseDTO(token, user.getName(), user.getEmail());
    }
}
