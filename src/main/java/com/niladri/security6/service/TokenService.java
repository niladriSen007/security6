package com.niladri.security6.service;

import com.niladri.security6.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class TokenService {

    @Value("${jwt.secretKey}")
    private String JWT_SECRET;

    private SecretKey generateSecretKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }


    public String generateToken(UserEntity user) {
        return Jwts.builder().subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("role", Set.of("USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(generateSecretKey())
                .compact();
        //The `compact()` method in the context of the `Jwts.builder()`
        // is used to finalize the JWT (JSON Web Token) creation process.
        // It serializes the JWT into its final compact form, which is a
        // string that can be easily transmitted over the network or stored.
        //Here’s the rewritten code with an explanation:
        // Finalizes the JWT creation by serializing it into a compact, URL-safe string
    }

    public Long getUserIdFromToken(String token) {
        return
                Long.parseLong(
                        Jwts.parser()                      // 1. Create a JWT parser instance
                                .verifyWith(generateSecretKey()) // 2. Set the secret key to verify the token's signature
                                .build()                        // 3. Build the parser
                                .parseSignedClaims(token)       // 4. Parse the token and extract the signed claims
                                .getPayload()                   // 5. Get the payload (body) of the JWT
                                .getSubject()                   // 6. Extract the subject (user ID) from the payload
                );
    }
}
