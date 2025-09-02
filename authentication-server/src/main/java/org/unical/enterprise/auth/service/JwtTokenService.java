package org.unical.enterprise.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unical.enterprise.auth.config.TokenProperties;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// @Service
public class JwtTokenService {

    @Getter
    private final TokenProperties tokenProperties;
    private final SecretKey secretKey;

    public JwtTokenService(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
        this.secretKey = Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(tokenProperties.getSecret()));
    }

    public String generateToken(Authentication authentication) {

        // Impostazioni Temporali
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS); // Creato adesso
        Instant notBefore = issuedAt.plus(5, ChronoUnit.SECONDS); // Valido tra 5 secondi
        Instant expiration = issuedAt.plus(this.tokenProperties.getExpiration(), ChronoUnit.SECONDS);

        // Claims' Set
        Map<String, Object> claims = new HashMap<>();

        // Aggiungi Campi di Riconoscimento
        claims.put("username", authentication.getName());
        claims.put("preferred_username", authentication.getName());

        // Aggiungi lo scope (per /userinfo)
        claims.put("scope", String.join(" ", this.tokenProperties.getScopes()));

        // Aggiungi le Authorities
        claims.put("roles", authentication.getAuthorities()
                .stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet())
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuer(tokenProperties.getIssuer())
                .setIssuedAt(Date.from(issuedAt))
                .setNotBefore(Date.from(notBefore))
                .setExpiration(Date.from(expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

}
