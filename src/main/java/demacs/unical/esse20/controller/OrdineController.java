package demacs.unical.esse20.controller;

import demacs.unical.esse20.dao.BigliettoDao;
import demacs.unical.esse20.dao.OrdineDao;
import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import demacs.unical.esse20.dto.BigliettoDto;
import demacs.unical.esse20.dto.OrdineDto;
import demacs.unical.esse20.dto.OrdineRequest;
import demacs.unical.esse20.service.OrdineService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/ordine")
@AllArgsConstructor
public class OrdineController {

    OrdineDao ordineDao;
    BigliettoDao bigliettoDao;

    OrdineService ordineService;

    @RequestMapping(method = RequestMethod.GET)
    private List<Ordine> findAll(){
        return ordineDao.findAll();
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    private Ordine findById(@PathVariable("id") String id){
         if (ordineDao.findById(id).isPresent())
             return ordineDao.findById(id).get();
         return null;
    }

    @GetMapping(value="/utente")
    private List<Ordine> findAllByUtente(@RequestParam("utente") String utente){
        return ordineDao.findAllByUtenteId(utente);
    }

    @GetMapping(value="/biglietti")
    private List<Biglietto> findAllBigliettiByOrdine(@RequestParam("ordine") String ordine){
        if (ordineDao.findById(ordine).isPresent())
            return bigliettoDao.findAllByOrdine(ordineDao.findById(ordine).get());
        return null;
    }

    @PostMapping(value="/save")
    private ResponseEntity<String> createOrdine(@RequestBody OrdineRequest ordineRequest){
        OrdineDto ordine = ordineRequest.ordine();
        List<BigliettoDto> biglietti = ordineRequest.biglietti();


        Ordine newOrdine = new Ordine();
        newOrdine.setUtenteId(ordine.utenteId());
        newOrdine.setBiglietti_comprati(ordine.bigliettiComprati());
        newOrdine.setImporto(ordine.importo());
        newOrdine.setData_pagamento(ordine.dataPagamento());

        Set<Biglietto> newBiglietti = new HashSet<>();
        for(BigliettoDto bigliettoDto : biglietti){
            Biglietto newBiglietto = new Biglietto();

            newBiglietto.setOrdine(newOrdine);
            newBiglietto.setEmail(bigliettoDto.email());
            newBiglietto.setNome(bigliettoDto.nome());
            newBiglietto.setCognome(bigliettoDto.cognome());
            newBiglietto.setData_nascita(bigliettoDto.dataNascita());
            newBiglietto.setE_valido(bigliettoDto.eValido());
            newBiglietto.setId_evento(bigliettoDto.idEvento());
            newBiglietti.add(newBiglietto);
        }

        ordineService.saveBiglietti(newOrdine, newBiglietti);
        return new ResponseEntity<>("Ordine Creato Correttamente", HttpStatus.OK);
    }

}
