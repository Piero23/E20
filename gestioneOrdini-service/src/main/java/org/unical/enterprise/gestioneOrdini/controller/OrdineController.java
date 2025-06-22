package org.unical.enterprise.gestioneOrdini.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;
import org.unical.enterprise.gestioneOrdini.dto.BigliettoDto;
import org.unical.enterprise.gestioneOrdini.dto.OrdineDto;
import org.unical.enterprise.gestioneOrdini.dto.OrdineRequest;
import org.unical.enterprise.gestioneOrdini.service.BigliettoService;
import org.unical.enterprise.gestioneOrdini.service.OrdineService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordine")
@AllArgsConstructor
public class OrdineController {

    private static final Logger logger = LoggerFactory.getLogger(OrdineController.class);

    OrdineService ordineService;
    BigliettoService bigliettoService;

    @GetMapping
    private ResponseEntity<List<Ordine>> findAll(){
        logger.info("Effettuata ricerca generale");
        return ResponseEntity.ok(ordineService.findAll());
    }

    @PostMapping("/save")
    private void save(@Valid @RequestBody OrdineRequest ordineRequest){
        ordineService.saveOrdine(ordineRequest.ordine(), ordineRequest.biglietti());
    }

    @GetMapping(value="/{id}")
    private ResponseEntity<Ordine> findById(@PathVariable("id") UUID id){
        logger.info("Ricevuta richiesta di ricerca ordine tramite ID Ordine");

        if (ordineService.findById(id)!=null) {
            String user= SecurityContextHolder.getContext().getAuthentication().getName();
            String orderId=ordineService.findById(id).getId().toString();
            if (user.equals(orderId)) {
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
    private ResponseEntity<List<Ordine>> findAllByUtente(@RequestParam("utente") UUID utente){
        logger.info("Ricevuta richiesta di ricerca Ordini tramite ID Utente");

        String user= SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.equals(utente.toString())) {
            logger.info("Ricerca andata a buon fine");
            return ResponseEntity.ok(ordineService.findAllByUtente(utente));
        }
        else{
            logger.info("Utente {} non autorizzato", user);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(value="/biglietti")
    private ResponseEntity<List<Biglietto>> findAllBigliettiByOrdine(@RequestParam("ordine") UUID ordine){
        logger.info("Ricevuta richiesta di ricerca Biglietti tramite ID Ordine");

        if (ordineService.findById(ordine)!=null) {
            String user= SecurityContextHolder.getContext().getAuthentication().getName();
            String utenteOrderId=ordineService.findById(ordine).getUtenteId().toString();

            if (user.equals(utenteOrderId)) {
                logger.info("Ricerca andata a buon fine");
                return ResponseEntity.ok(bigliettoService.findAllByOrdine(ordineService.findById(ordine)));
            }
            else{
                logger.info("Utente {} non autorizzato", user);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            //non so se si potrebbe spostare tutta la funzione nell'ordine service o se è più pulito così
        }
        logger.info("Nessun dato");
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/test")
    private String test() {
        return "Sono OrdineController";
    }

}
