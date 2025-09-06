package org.unical.enterprise.gestioneOrdini.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;
import org.unical.enterprise.shared.dto.OrdineRequest;
import org.unical.enterprise.gestioneOrdini.service.BigliettoService;
import org.unical.enterprise.gestioneOrdini.service.OrdineService;
import org.unical.enterprise.shared.clients.MailServiceClient;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordine")
@AllArgsConstructor
public class OrdineController {

    private static final Logger logger = LoggerFactory.getLogger(OrdineController.class);
    private final CharacterEncodingFilter characterEncodingFilter;

    OrdineService ordineService;
    BigliettoService bigliettoService;
    MailServiceClient mailService;

    @GetMapping
    private ResponseEntity<List<Ordine>> findAll(){
        logger.info("Effettuata ricerca generale");
        return ResponseEntity.ok(ordineService.findAll());
    }

    @PostMapping
    private ResponseEntity<String> save(@Valid @RequestBody OrdineRequest ordineRequest){
        ordineService.saveOrdine(ordineRequest.ordine(), ordineRequest.biglietti());
        return ResponseEntity.ok("Ordine creato con successo");
    }

    @GetMapping(value="/{id}")
    private ResponseEntity<Ordine> findById(@PathVariable("id") UUID id, Authentication auth){
        logger.info("Ricevuta richiesta di ricerca ordine tramite ID Ordine");

        if (ordineService.findById(id)!=null) {
            UUID user = ordineService.getUserIDByUsername(auth.getName());
            UUID orderUserId = ordineService.findById(id).getUtenteId();
            if (user.equals(orderUserId)) {
                logger.info("Ricerca andata a buon fine");
                return ResponseEntity.ok(ordineService.findById(id));
            }
            else {
                logger.info("Utente {} non autorizzato", user);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        logger.info("Nessun dato");
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value="/utente")
    private ResponseEntity<List<Ordine>> findAllByUtente(@RequestParam("utente") UUID utente, Authentication auth){
        logger.info("Ricevuta richiesta di ricerca Ordini tramite ID Utente");
        UUID user = ordineService.getUserIDByUsername(auth.getName());
        if (user.equals(utente)) {
            logger.info("Ricerca andata a buon fine");
            return ResponseEntity.ok(ordineService.findAllByUtente(user));
        }
        else{
            logger.info("Utente {} non autorizzato", user);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(value="/biglietti")
    private ResponseEntity<List<Biglietto>> findAllBigliettiByOrdine(@RequestParam("ordine") UUID ordine, Authentication auth){
        logger.info("Ricevuta richiesta di ricerca Biglietti tramite ID Ordine");

        if (ordineService.findById(ordine)!=null) {
            UUID user = ordineService.getUserIDByUsername(auth.getName());
            UUID utenteOrderId=ordineService.findById(ordine).getUtenteId();

            if (user.equals(utenteOrderId)) {
                logger.info("Ricerca andata a buon fine");
                return ResponseEntity.ok(bigliettoService.findAllByOrdine(ordineService.findById(ordine)));
            }
            else{
                logger.info("Utente {} non autorizzato", user);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        logger.info("Nessun dato");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/test")
    private String test() {
        return "Sono OrdineController";
    }

    @GetMapping("/testFeign")
    private String testo() {
        return mailService.test();
    }

}
