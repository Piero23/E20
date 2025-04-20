package demacs.unical.esse20.controller;

import com.nimbusds.jose.JOSEException;

import demacs.unical.esse20.domain.Ruolo;
import demacs.unical.esse20.domain.Utente;

import demacs.unical.esse20.dto.UtenteLoginDto;
import demacs.unical.esse20.dto.UtenteRegistrationDto;

import demacs.unical.esse20.security.JwtTokenService;
import demacs.unical.esse20.service.UtenteService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/v1", produces = "application/json")
@CrossOrigin(origins = "http://localhost:${server.port}")
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @PostMapping(path="/register") // Deve essere RequestBody
    public ResponseEntity<String> register(@RequestBody UtenteRegistrationDto utenteDto) {
        System.out.println("Trying to register user " + utenteDto.getUsername());
        if (utenteService.getUserByUsername(utenteDto.getUsername()).isPresent())
            return new ResponseEntity<>("Username gia' in uso", HttpStatus.CONFLICT);

        Utente newUser = new Utente();
        newUser.setUsername(utenteDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(utenteDto.getPassword()));
        newUser.setEmail("temp@email.com");
        newUser.setRuolo(Ruolo.USER);
        newUser.setDataNascita(LocalDate.now());

        utenteService.save(newUser);

        return new ResponseEntity<>("Utente Registrato Correttamente", HttpStatus.OK);
    }


    @PostMapping(path = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public void authenticate(@RequestBody UtenteLoginDto utenteDto,
                             HttpServletResponse response) throws JOSEException {


        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(utenteDto.getUsername(), utenteDto.getPassword()));
        String token = jwtTokenService.createToken(utenteDto.getUsername());
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    @GetMapping(path="/users/{username}")
    // Only the user and the admin can see user details
    @PreAuthorize("#username.equals(authentication.principal.getUsername()) or hasRole('ADMIN')")
    public String getUser(@PathVariable("username") String username) {
        Optional<Utente> utente = utenteService.getUserByUsername(username);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", (utente.isPresent()) ? utente.get().getUsername() : "");
        return jsonObject.toString();
    }

    @GetMapping(path="/users")
    @PreAuthorize("hasRole('ADMIN')") // Only admin can see all the users
    public Iterable<Utente> users() { return utenteService.getAllUsers(); }

}
