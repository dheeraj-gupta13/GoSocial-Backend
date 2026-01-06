package com.goSocial.goSocial.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    private final String SECRET = "VJYw5kMZpR3yM4MZx9p9m+9vU7n8x9x8XJc7L4kA+9w=";

    public String generateToken(int  user_id) {
//        System.out.println("");
        return Jwts.builder()
                .setSubject(String.valueOf(user_id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

//    public String extractUsername(String token) {
//        return getClaims(token).getSubject();
//    }

    public int extractUserId(String token){
        return  Integer.parseInt(getClaims(token).getSubject());
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
