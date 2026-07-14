package com.zagrebopenfestivals.service.impl;

import com.zagrebopenfestivals.dto.request.LoginRequest;
import com.zagrebopenfestivals.dto.request.RegisterRequest;
import com.zagrebopenfestivals.dto.response.AuthResponse;
import com.zagrebopenfestivals.entity.RefreshToken;
import com.zagrebopenfestivals.entity.Role;
import com.zagrebopenfestivals.entity.User;
import com.zagrebopenfestivals.exception.DuplicateResourceException;
import com.zagrebopenfestivals.exception.InvalidRefreshTokenException;
import com.zagrebopenfestivals.repository.RefreshTokenRepository;
import com.zagrebopenfestivals.repository.UserRepository;
import com.zagrebopenfestivals.security.JwtTokenProvider;
import com.zagrebopenfestivals.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final long refreshExpirationMs;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public AuthServiceImpl(UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username je već zauzet: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email je već registriran: " + request.email());
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public LoginResult login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalStateException("Korisnik nije pronađen nakon uspješne autentifikacije"));

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String rawRefreshToken = issueRefreshToken(user);

        AuthResponse response = AuthResponse.of(accessToken, user.getUsername(), user.getRole().name());
        return new LoginResult(response, rawRefreshToken);
    }

    @Override
    @Transactional
    public RefreshResult refresh(String rawToken) {
        String tokenHash = hash(rawToken);

        RefreshToken existingToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (existingToken.isRevoked() || existingToken.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException();
        }

        User user = existingToken.getUser();

        // rotacija refresh tokena - stari se odmah povlači iz upotrebe
        existingToken.setRevoked(true);
        refreshTokenRepository.save(existingToken);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );

        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String newRawRefreshToken = issueRefreshToken(user);

        AuthResponse response = AuthResponse.of(newAccessToken, user.getUsername(), user.getRole().name());
        return new RefreshResult(response, newRawRefreshToken);
    }

    @Override
    @Transactional
    public void logout(String rawToken) {
        String tokenHash = hash(rawToken);
        refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    private String issueRefreshToken(User user) {
        String rawToken = generateSecureRandomToken();
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(hash(rawToken))
                .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    private String generateSecureRandomToken() {
        byte[] bytes = new byte[64];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 nije dostupan", e);
        }
    }
}