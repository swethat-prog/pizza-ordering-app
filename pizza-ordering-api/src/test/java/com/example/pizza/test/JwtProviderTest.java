package com.example.pizza.test;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.pizza.security.JwtProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import javax.crypto.SecretKey;
import java.util.Date;

@SpringBootTest
class JwtProviderTest {

    @InjectMocks
    private JwtProvider jwtProvider;

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Mock
    private SecretKey mockSecretKey;

    private static final String USERNAME = "testUser";
    private String token;

    @BeforeEach
    void setUp() {
        // You can mock the secret key behavior if needed for testing
        when(mockSecretKey.getEncoded()).thenReturn(secretKey.getBytes());

        jwtProvider = new JwtProvider(secretKey, 3600000); // Example expiration time of 1 hour
        token = jwtProvider.generateToken(USERNAME);
    }

    @Test
    void generateToken_ShouldGenerateToken() {
        // Ensure the token is not null and contains the username
        assertNotNull(token);
        assertTrue(token.contains(USERNAME));
    }

    @Test
    void getUsernameFromToken_ShouldReturnUsername() {
        String extractedUsername = jwtProvider.getUsernameFromToken(token);

        // Verify that the extracted username matches the one used during token generation
        assertEquals(USERNAME, extractedUsername);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        boolean isValid = jwtProvider.validateToken(token);

        // Assert that the token is valid
        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForExpiredToken() {
        // Simulate an expired token
        Date expiredDate = new Date(System.currentTimeMillis() - 3600000); // Set the expiration to 1 hour ago
        String expiredToken = Jwts.builder()
                .setSubject(USERNAME)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(mockSecretKey, SignatureAlgorithm.HS512)
                .compact();

        boolean isValid = jwtProvider.validateToken(expiredToken);

        // Assert that the expired token is invalid
        assertFalse(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidToken() {
        String invalidToken = "invalidToken123";

        boolean isValid = jwtProvider.validateToken(invalidToken);

        // Assert that an invalid token is not valid
        assertFalse(isValid);
    }
}
