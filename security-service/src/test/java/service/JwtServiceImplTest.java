package service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.SecurityServiceApplication;
import org.example.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SecurityServiceApplication.class)
@AutoConfigureMockMvc
public class JwtServiceImplTest {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Mock
    private UserDetails userDetails;

    @Autowired
    private JwtServiceImpl jwtService;

    @Test
    public void testExtractUserName() {
        String token = generateTokenWithSubject("testuser");

        String extractedUserName = jwtService.extractUserName(token);

        assertEquals("testuser", extractedUserName);
    }

    @Test
    public void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String generatedToken = jwtService.generateToken(userDetails);

        assertNotNull(generatedToken);
    }

    @Test
    public void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

//    @Test
//    public void testIsTokenExpired() {
//        Date expiration = new Date(System.currentTimeMillis() - 1000);
//        String token = generateTokenWithExpiration(expiration);
//
//        boolean isExpired = jwtService.isTokenExpired(token);
//
//        assertTrue(isExpired);
//    }

    private String generateTokenWithSubject(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(getSigningKey())
                .compact();
    }

    private String generateTokenWithExpiration(Date expiration) {
        return Jwts.builder()
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
