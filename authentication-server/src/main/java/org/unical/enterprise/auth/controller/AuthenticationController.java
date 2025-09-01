package org.unical.enterprise.auth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.auth.data.dto.UtenteRegistrationDTO;
import org.unical.enterprise.auth.data.model.UtenteAuth;
import org.unical.enterprise.auth.exceptions.UserProfileCreationException;
import org.unical.enterprise.auth.exceptions.UsernameAlreadyExistsException;
import org.unical.enterprise.auth.service.AuthService;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

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

    @DeleteMapping("/api/utente/me/temp")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        String username = authentication.getName();
        authService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("auth/ciao")
    String greeting() {
        return "Ciao";
    }
}
