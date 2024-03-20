package com.qsp.todo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private String jwtExpirationDate;

    private static final long JWT_EXPIRATION_DURATION_MS = 864_000_000;

//    public String generateToken(Authentication authentication){
//        String username = authentication.getName();
//
//        // Set the expiration date
//        Date currentDate = new Date();
//        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
//
//        // Generate the token
//        String token = Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(currentDate) // Use the current date as the issue date
//                .setExpiration(expireDate) // Set the calculated expiration date
//                .signWith(key())
//                .compact();
//
//        return token;
//    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        // Set the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Calculate the expiration date and time
        LocalDateTime expireDateTime = currentDateTime.plus(JWT_EXPIRATION_DURATION_MS, ChronoUnit.MILLIS);

        // Generate the token
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(java.sql.Date.valueOf(currentDateTime.toLocalDate())) // Convert to java.sql.Date
                .setExpiration(java.sql.Date.valueOf(expireDateTime.toLocalDate())) // Convert to java.sql.Date
                .signWith(key())
                .compact();

        return token;
    }
    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    public String getUsername(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username=claims.getSubject();
        return username;
    }

    public boolean validateToken(String token){
        Jwts.parser()
                .setSigningKey(key())
                .build()
                .parse(token);
        return true;
    }
}
