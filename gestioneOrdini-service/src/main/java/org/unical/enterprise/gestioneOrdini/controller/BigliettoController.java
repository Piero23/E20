package org.unical.enterprise.gestioneOrdini.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.shared.dto.BigliettoDto;
import org.unical.enterprise.gestioneOrdini.service.BigliettoService;
import org.unical.enterprise.shared.dto.TicketCheck;

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

    @GetMapping(value="/{id}/qr")
    private ResponseEntity<Map<String, String>> getQrCode(@PathVariable UUID id) throws URISyntaxException {
        logger.info("Richiesta generazione QrCode Ordine");
        Map<String, String> response = new HashMap<>();
        response.put("imageBase64", bigliettoService.getQrCode(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/{id}")
    private ResponseEntity<String> validate(@PathVariable UUID id, Authentication auth) {
        logger.info("Inizio Validazione ticket "+ id);
        UUID user = bigliettoService.getUserIDByUsername(auth.getName());
        UUID eventoOrganizzatoreUUID = bigliettoService.getOrganizzatoreEventoByTicket(id);
        if (user.equals(eventoOrganizzatoreUUID)) {
            if (bigliettoService.checkExists(id)){
                if (bigliettoService.validate(id)){
                    return ResponseEntity.ok("BIglietto validato correttamente");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Biglietto già utilizzato");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Biglietto non esistente");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Impossibile validare biglietto per evento non proprietario");
    }

    @GetMapping("/evento")
    public List<BigliettoDto> getBigliettoEvento(@RequestParam Long id) {
        return bigliettoService.findAllByEvento(id);
    }

    @GetMapping("/test")
    private String test() {
        return "Sono BigliettoController";
    }

    @PostMapping("/duplicate")
    public boolean findByData(@RequestBody TicketCheck ticketCheck){
        return bigliettoService.findTicketByData(ticketCheck.eMail(), ticketCheck.eventID());
    }
}
