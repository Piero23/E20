package org.unical.enterprise.gestioneOrdini.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unical.enterprise.gestioneOrdini.dao.OrdineDao;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;
import org.unical.enterprise.gestioneOrdini.dto.BigliettoDto;
import org.unical.enterprise.gestioneOrdini.dto.OrdineDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdineService {

    private final OrdineDao ordineDao;

    @Transactional(readOnly = true)
    public void test() {
/*
        Ordine ordine =  new Ordine("117c2dcb-d492-4dd6-b349-8db6a021038c", 5, 10.5F, new Date(10,10,10));

        Biglietto b = new Biglietto(2L,"AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",true,"cia","caa",new Date(10,10,10));

        ordine.addBiglietto(b);

        ordineDao.save(ordine);


 */
    }

    @Transactional
    public List<Ordine> findAll() {
        return ordineDao.findAll();
    }

    @Transactional
    public Ordine saveOrdine(OrdineDto ordine, List<BigliettoDto> biglietti) {
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

        newOrdine.getBiglietti().addAll(newBiglietti);
        ordineDao.save(newOrdine);
        return newOrdine;
    }

    @Transactional
    public Ordine findById(UUID id) {
        if (ordineDao.findById(id).isPresent()){
            return ordineDao.findById(id).get();
        }
        return null;
    }

    @Transactional
    public List<Ordine> findAllByUtente(UUID utente) {
        return ordineDao.findAllByUtenteId(utente);
    }


}