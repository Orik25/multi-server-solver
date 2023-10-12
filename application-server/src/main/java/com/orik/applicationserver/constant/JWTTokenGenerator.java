package com.orik.applicationserver.constant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;

public class JWTTokenGenerator {
    public static String generateToken(Authentication authentication) {
        String secretKey = SecurityConstants.JWT_KEY;
        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());
        claims.put("role", authentication.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
