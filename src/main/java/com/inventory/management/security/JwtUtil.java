package com.inventory.management.security;

import com.inventory.management.dto.LoginResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {
    private Key key;

//    @Value("${jwt.expiration}")
//    private Long expirationTime;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // put username inside token, set issue and expiry time, sign token with key
    public LoginResponseDTO generateToken(String username) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + 900000);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return new LoginResponseDTO(token, issuedAt, expiresAt);
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // extract other details from token
    private Claims extractClaims(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
