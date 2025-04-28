package demacs.unical.esse20.controller;

import demacs.unical.esse20.data.domain.Utente;

import demacs.unical.esse20.service.UtenteService;
import lombok.AllArgsConstructor;

import org.json.JSONObject;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="/api/v1", produces = "application/json")
@CrossOrigin(origins = "http://localhost:${server.port}")
@AllArgsConstructor
public class UtenteController2 {

    private final UtenteService utenteService;

    @GetMapping(path="/users/{username}")
    @PreAuthorize("#username.equals(authentication.principal.getUsername()) or hasRole('ADMIN')")
    public String getUser(@PathVariable("username") String username) {
        Optional<Utente> utente = utenteService.getUserByUsername(username);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", (utente.isPresent())? utente.get().toString() : "Utente Non Trovato");
        return jsonObject.toString();
    }

    @GetMapping(path="/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<Utente> users() { return utenteService.getAllUsers(); }

}
