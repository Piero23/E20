package demacs.unical.esse20.controller;

import demacs.unical.esse20.dao.BigliettoDao;
import demacs.unical.esse20.dao.OrdineDao;
import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import demacs.unical.esse20.dto.BigliettoDto;
import demacs.unical.esse20.dto.OrdineDto;
import demacs.unical.esse20.dto.OrdineRequest;
import demacs.unical.esse20.service.BigliettoService;
import demacs.unical.esse20.service.OrdineService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/ordine")
@AllArgsConstructor
public class OrdineController {

    OrdineService ordineService;
    BigliettoService bigliettoService;

    @GetMapping
    private ResponseEntity<List<Ordine>> findAll(){
        return ResponseEntity.ok(ordineService.findAll());
    }

    @GetMapping(value="/{id}")
    private ResponseEntity<Ordine> findById(@PathVariable("id") String id){
        if (ordineService.findById(id)!=null)
            return ResponseEntity.ok(ordineService.findById(id));
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value="/utente")
    private ResponseEntity<List<Ordine>> findAllByUtente(@RequestParam("utente") String utente){
         return ResponseEntity.ok(ordineService.findAllByUtente(utente));
    }

    @GetMapping(value="/biglietti")
    private ResponseEntity<List<Biglietto>> findAllBigliettiByOrdine(@RequestParam("ordine") String ordine){
        if (ordineService.findById(ordine)!=null)
            return ResponseEntity.ok(bigliettoService.findAllByOrdine(ordineService.findById(ordine)));
            //non so se si potrebbe spostare tutta la funzione nell'ordine service o se è più pulito così
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value="/save")
    private ResponseEntity<String> createOrdine(@RequestBody OrdineRequest ordineRequest){
        ordineService.saveOrdine(ordineRequest.ordine(), ordineRequest.biglietti());
        return new ResponseEntity<>("Ordine Creato Correttamente", HttpStatus.OK);
    }

}
