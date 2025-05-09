package demacs.unical.esse20.controller;

import demacs.unical.esse20.dao.BigliettoDao;
import demacs.unical.esse20.dao.OrdineDao;
import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import demacs.unical.esse20.dto.BigliettoDto;
import demacs.unical.esse20.dto.OrdineDto;
import demacs.unical.esse20.dto.OrdineRequest;
import demacs.unical.esse20.service.BigliettoService;
import demacs.unical.esse20.service.MailService;
import demacs.unical.esse20.service.OrdineService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ordine")
@AllArgsConstructor
public class OrdineController {

    private static final Logger logger = LoggerFactory.getLogger(OrdineController.class);

    OrdineService ordineService;
    BigliettoService bigliettoService;
    MailService mailService;

    @GetMapping
    private ResponseEntity<List<Ordine>> findAll(){
        return ResponseEntity.ok(ordineService.findAll());
    }

    @GetMapping(value="/{id}")
    private ResponseEntity<Ordine> findById(@PathVariable("id") UUID id){
        logger.info("Ricevuta richiesta di ricerca ordine tramite ID Ordine");
        if (ordineService.findById(id)!=null)
            return ResponseEntity.ok(ordineService.findById(id));
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value="/utente")
    private ResponseEntity<List<Ordine>> findAllByUtente(@RequestParam("utente") UUID utente){
        logger.info("Ricevuta richiesta di ricerca Ordini tramite ID Utente");
        return ResponseEntity.ok(ordineService.findAllByUtente(utente));
    }

    @GetMapping(value="/biglietti")
    private ResponseEntity<List<Biglietto>> findAllBigliettiByOrdine(@RequestParam("ordine") UUID ordine){
        logger.info("Ricevuta richiesta di ricerca Biglietti tramite ID Ordine");
        if (ordineService.findById(ordine)!=null)
            return ResponseEntity.ok(bigliettoService.findAllByOrdine(ordineService.findById(ordine)));
            //non so se si potrebbe spostare tutta la funzione nell'ordine service o se è più pulito così
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value="/save")
    private ResponseEntity<String> createOrdine(@RequestBody OrdineRequest ordineRequest){
        logger.info("Inizio Salvataggio Ordine");
        Ordine ordine=ordineService.saveOrdine(ordineRequest.ordine(), ordineRequest.biglietti());

        //ottenere mail utente acquirente
        //mailService.sendMail(mail utente acquirente, ordine);

        for(Biglietto b: bigliettoService.findAllByOrdine(ordine)){
            mailService.sendQrCodeMail(b.getEmail(), bigliettoService.getQrCode(b.getId()));
        }
        logger.info("Ordine {} Salvato", ordine.getId());
        return new ResponseEntity<>("Ordine Creato Correttamente", HttpStatus.OK);
    }

}
