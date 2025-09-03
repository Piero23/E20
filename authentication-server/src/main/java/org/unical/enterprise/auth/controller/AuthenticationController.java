package org.unical.enterprise.auth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.auth.data.dto.UtenteRegistrationDTO;
import org.unical.enterprise.auth.data.model.UtenteAuth;
import org.unical.enterprise.auth.exceptions.UserProfileCreationException;
import org.unical.enterprise.auth.exceptions.UsernameAlreadyExistsException;
import org.unical.enterprise.auth.service.AuthService;
import org.unical.enterprise.shared.dto.UtenteAuthDTO;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
//    private final JwtTokenService jwtTokenService;
//    private final OAuth2AuthorizationService authorizationService;
//    private final OAuth2TokenGenerator<?> tokenGenerator;
//    private final RegisteredClientRepository registeredClientRepository;

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody UtenteRegistrationDTO utenteRegistrationDTO) {

        try {
            UtenteAuth utenteAuth = authService.registerNewUser(utenteRegistrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (UserProfileCreationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody UtenteAuthDTO utenteAuthDTO) {
        return ResponseEntity.ok().build();
//        // 1. Autentica l’utente
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        utenteAuthDTO.getUsername(),
//                        utenteAuthDTO.getPassword()
//                )
//        );
//
//        // 2. Recupera il client (ad esempio "gateway-client")
//        RegisteredClient registeredClient = registeredClientRepository.findByClientId("gateway-client");
//        if (registeredClient == null) {
//            return ResponseEntity.badRequest().body("Client non registrato");
//        }
//
//        // 3. Costruisci il token request per Authorization Server
//        OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
//                .registeredClient(registeredClient)
//                .principal(authentication)
//                .authorizedScopes(registeredClient.getScopes())
//                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrant(new OAuth2AuthorizationGrantAuthenticationToken(
//                        AuthorizationGrantType.CLIENT_CREDENTIALS, authentication
//                ))
//                .build();
//        // 4. Genera il token tramite il token generator integrato
//        OAuth2AccessToken accessToken = (OAuth2AccessToken) tokenGenerator.generate(tokenContext);
//
//        if (accessToken == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Errore generazione    token");
//        }
//
//        // 5. Ritorna il token come JSON
//        Map<String, Object> response = Map.of(
//                "access_token", accessToken.getTokenValue(),
//                "token_type", accessToken.getTokenType().getValue(),
//                "expires_in", accessToken.getExpiresAt().getEpochSecond() - Instant.now().getEpochSecond(),
//                "scope", accessToken.getScopes()
//        );
//
//        return ResponseEntity.ok(response);
//    }


//        // Autentica l'Utente
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(utenteAuthDTO.getUsername(), utenteAuthDTO.getPassword())
//        );
//
//        // Genera Token
//        String accessToken = jwtTokenService.generateToken(authentication);
//
//        // Response Body
//        Map<String, Object> responseBody = Map.of(
//                "access_token", accessToken,
//                "token_type", "Bearer",
//                "expires_in", jwtTokenService.getTokenProperties().getExpiration()
//        );
//
//        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/api/utente/me")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        String username = authentication.getName();
        authService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth/ciao")
    String greeting() {
        return "Ciao";
    }

    @GetMapping("/auth/ciao/auth")
    String greeting(Authentication auth) {
        return "Ciao " + auth.getName();
    }

}
