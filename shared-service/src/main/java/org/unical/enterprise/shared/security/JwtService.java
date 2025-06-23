package org.unical.enterprise.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * Gestisce la creazione, validazione ed estrazione di informazioni dai token JWT.
 */
public class JwtService {
    
    private String jwtSecret;
    
    private long jwtExpiration = 3600000; // Default 1 ora
    
    private long refreshExpiration = 86400000; // Default 24 ore

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public void setJwtExpiration(long jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }

    public void setRefreshExpiration(long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    /**
     * Estrae le informazioni dell'utente dal token JWT
     */
    public JwtUserClaims extractUserClaims(String token) {
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();


        List<String> authorities = new ArrayList<>();

        Map<String, Object> resourceAccess = claims.get("resource_access", Map.class);
        if (resourceAccess != null) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("esse20-client");
            if (clientAccess != null) {
                Object rolesObject = clientAccess.get("roles");
                if (rolesObject instanceof List<?>) {
                    for (Object role : (List<?>) rolesObject) {
                        if (role instanceof String) {
                            authorities.add("ROLE_" + role);
                        }
                    }
                }
            }
        }


        return new JwtUserClaims(username, authorities);
    }

    /**
     * Estrae lo username dal token JWT
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Estrae una specifica informazione dalle claims del token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Genera un token JWT per un utente
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(extractClaims(userDetails), userDetails);
    }

    /**
     * Genera un token JWT con claims personalizzate
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Genera un refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(extractClaims(userDetails), userDetails, refreshExpiration);
    }

    /**
     * Costruisce il token JWT
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Estrae le claims dell'utente dai dettagli utente
     */
    private Map<String, Object> extractClaims(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
                
        return Map.of("roles", roles);
    }

    /**
     * Verifica se il token è valido per l'utente specificato
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Verifica se il token è valido (non scaduto e firmato correttamente)
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se il token è scaduto
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Estrae la data di scadenza dal token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Estrae tutte le claims dal token JWT
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Ottiene la chiave per la firma JWT
     */
    private Key getSigningKey() {
        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyds99Sr9Jy2b6aehdgSRf6n+B4lvB5ITFBQj5JNleg1FsvSCECXpkcdEO27F56JX8HK95vgV2Mnv9/NcWasO254R0wyjlSUDMrLJIs7r4IEAA9CpPy6aitfvLMhIwNgg4HHlMnO1PJ5wWACjC4uMne3jTSefRybQkxf+sNSlw3zJLPNOBn+Zl8D4PmhvQVNKj+Tt49zBvauxCIt0cBFBvxpiDkOyBx873ph1WjZBmVzTyCybxF0ebrLCjg8JjmJQabXPaoIB2V6aV3P1PN2itiENKUXEtaq/eveoLj0opb14/foU/ObgCYhAHzjFcZ+l77uS1kRvj8NWLRWxOc0KAwIDAQAB";
        try{
            byte[] keyBytes = Decoders.BASE64.decode(pubKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Errore nella creazione della chiave pubblica", e);
        }
    }
}