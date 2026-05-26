package com.duy.hospital.authservice.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duy.hospital.authservice.config.RefreshTokenProperties;
import com.duy.hospital.authservice.dto.request.LoginRequest;
import com.duy.hospital.authservice.dto.request.LogoutRequest;
import com.duy.hospital.authservice.dto.request.RefreshTokenRequest;
import com.duy.hospital.authservice.dto.response.AuthResponse;
import com.duy.hospital.authservice.dto.response.UserResponse;
import com.duy.hospital.authservice.entity.RefreshToken;
import com.duy.hospital.authservice.entity.User;
import com.duy.hospital.authservice.entity.UserStatus;
import com.duy.hospital.authservice.exception.ApiException;
import com.duy.hospital.authservice.exception.ErrorCode;
import com.duy.hospital.authservice.mapper.UserMapper;
import com.duy.hospital.authservice.repository.RefreshTokenRepository;
import com.duy.hospital.authservice.repository.UserRepository;

@Service
public class AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final TokenHashService tokenHashService;
    private final RefreshTokenProperties refreshTokenProperties;
    private final UserMapper userMapper;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenGenerator refreshTokenGenerator,
            TokenHashService tokenHashService,
            RefreshTokenProperties refreshTokenProperties,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.tokenHashService = tokenHashService;
        this.refreshTokenProperties = refreshTokenProperties;
        this.userMapper = userMapper;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw invalidCredentials();
        }

        ensureUserCanLogin(user);
        return issueTokenPair(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        String tokenHash = tokenHashService.sha256(request.refreshToken());
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(this::invalidRefreshToken);

        Instant now = Instant.now();
        if (refreshToken.isRevoked() || refreshToken.isExpired(now)) {
            throw invalidRefreshToken();
        }

        User user = refreshToken.getUser();
        ensureUserCanLogin(user);

        refreshToken.setRevokedAt(now);
        return issueTokenPair(user);
    }

    @Transactional
    public void logout(LogoutRequest request) {
        String tokenHash = tokenHashService.sha256(request.refreshToken());
        refreshTokenRepository.findByTokenHash(tokenHash)
                .filter(token -> !token.isRevoked())
                .ifPresent(token -> token.setRevokedAt(Instant.now()));
    }

    @Transactional(readOnly = true)
    public UserResponse me(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND, "User not found"));
        return userMapper.toResponse(user);
    }

    private AuthResponse issueTokenPair(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenValue = refreshTokenGenerator.generate();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(tokenHashService.sha256(refreshTokenValue));
        refreshToken.setExpiresAt(Instant.now().plus(refreshTokenProperties.ttlDays(), ChronoUnit.DAYS));
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                accessToken,
                refreshTokenValue,
                TOKEN_TYPE,
                jwtService.accessTokenExpiresInSeconds(),
                userMapper.toResponse(user)
        );
    }

    private void ensureUserCanLogin(User user) {
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorCode.USER_DISABLED, "User is not active");
        }
    }

    private ApiException invalidCredentials() {
        return new ApiException(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_CREDENTIALS, "Invalid email or password");
    }

    private ApiException invalidRefreshToken() {
        return new ApiException(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_REFRESH_TOKEN, "Invalid refresh token");
    }
}
