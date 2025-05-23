package org.unical.enterprise.gestioneOrdini.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.service.BigliettoService;

import java.net.URISyntaxException;
import java.util.*;

//decidere se rimuovere completamente la classe

@RestController
@RequestMapping("/api/biglietto")
@AllArgsConstructor
public class BigliettoController {

    private final BigliettoService bigliettoService;

    private static final Logger logger = LoggerFactory.getLogger(BigliettoController.class);

    @GetMapping
    private ResponseEntity<List<Biglietto>> getAllBiglietti() {
        List<Biglietto> response=new ArrayList<>();
        response.addAll(bigliettoService.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/{id}")
    private ResponseEntity<Map<String, String>> getQrCode(@PathVariable UUID id) throws URISyntaxException {
        //potenzialmente sostituibile nel backend

        //ritorna l'id del biglietto,
        //implementare la validazione allo scan
        logger.info("Richiesta generazione QrCode Ordine");
        Map<String, String> response = new HashMap<>();
        response.put("imageBase64", bigliettoService.getQrCode(id));
        return ResponseEntity.ok(response);
    }


    @GetMapping("/test")
    private String test() {
        return "Sono BigliettoController";
    }
}
