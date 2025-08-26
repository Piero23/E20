package org.unical.enterprise.mailSender.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/token")
@AllArgsConstructor
public class TokenController {

    private JwtDecoder jwtDecoder;

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser == null) {
            return ResponseEntity.ok(Map.of("error", "No OIDC user found"));
        }

        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("Subject", oidcUser.getSubject());
        userInfo.put("Username", oidcUser.getPreferredUsername());
        userInfo.put("Email", oidcUser.getEmail());
        userInfo.put("All Claims", oidcUser.getClaims());

        if (oidcUser.getIdToken() != null) {
            userInfo.put("ID Token", oidcUser.getIdToken().getTokenValue());
        }

        // Authorities
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            var authorities = authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toList());
            userInfo.put("Spring Authorities", authorities);
        }

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/testClaims")
    public ResponseEntity<Map<String, Object>> getClaims(
            @RegisteredOAuth2AuthorizedClient("custom-oidc") OAuth2AuthorizedClient client) {

        if (client == null || client.getAccessToken() == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No access token available"));
        }

        // Access token JWT
        String tokenValue = client.getAccessToken().getTokenValue();

        // Decodifica il token con il decoder di Spring
        Jwt jwt = jwtDecoder.decode(tokenValue);

        System.out.println(jwt.getTokenValue());
        return ResponseEntity.ok(jwt.getClaims());
    }

    @GetMapping("/claims")
    public ResponseEntity<Map<String, Object>> getClaims(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.ok(Map.of("error", "No JWT token found"));
        }

        return ResponseEntity.ok(jwt.getClaims());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getRoles(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.ok(List.of());
        }

        Object rolesObj = jwt.getClaim("roles");
        if (rolesObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) rolesObj;
            return ResponseEntity.ok(roles);
        }

        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated as: " + auth.getName() +
                    " with authorities: " + auth.getAuthorities());
        }
        return ResponseEntity.ok("Not authenticated");
    }
}