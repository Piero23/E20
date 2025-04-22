package demacs.unical.esse20.controller;

import com.nimbusds.jose.JOSEException;
import demacs.unical.esse20.dto.UtenteLoginDto;
import demacs.unical.esse20.dto.UtenteRegistrationDto;

import demacs.unical.esse20.security.JwtTokenService;
import demacs.unical.esse20.service.UtenteService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/v1/auth", produces = "application/json")
@CrossOrigin(origins = "http://localhost:${server.port}")
@AllArgsConstructor
public class AuthController {
    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @PostMapping(path="/register")
    public ResponseEntity<String> register(@RequestBody UtenteRegistrationDto utenteDto) {
        System.out.println("Trying to register user " + utenteDto.getUsername());

        if (utenteService.getUserByUsername(utenteDto.getUsername()).isPresent())
            return new ResponseEntity<>("Username gia' in uso", HttpStatus.CONFLICT);

        utenteService.registerNewUser(utenteDto);

        return new ResponseEntity<>("Utente Registrato Correttamente", HttpStatus.OK);
    }


    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody UtenteLoginDto utenteDto,
                             HttpServletResponse response) throws JOSEException{

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(utenteDto.getUsername(), utenteDto.getPassword()));
        String token = jwtTokenService.createToken(utenteDto.getUsername());
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }


}
