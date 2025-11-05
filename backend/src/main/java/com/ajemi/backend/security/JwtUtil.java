// package com.ajemi.backend.security;

// import io.jsonwebtoken.*;
// import org.springframework.stereotype.Component;
// import java.util.Date;

// @Component
// public class JwtUtil {

//     private final String SECRET_KEY = "monSecretTresFort12345";

//     public String generateToken(String email) {
//         return Jwts.builder()
//                 .setSubject(email)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 jour
//                 .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                 .compact();
//     }

//     public String extractEmail(String token) {
//         return Jwts.parser()
//                 .setSigningKey(SECRET_KEY)
//                 .parseClaimsJws(token)
//                 .getBody()
//                 .getSubject();
//     }
// }