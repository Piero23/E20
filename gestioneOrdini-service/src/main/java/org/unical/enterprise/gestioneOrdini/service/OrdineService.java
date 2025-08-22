package org.unical.enterprise.gestioneOrdini.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unical.enterprise.gestioneOrdini.EventoServiceClient;
import org.unical.enterprise.gestioneOrdini.dao.OrdineDao;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;
import org.unical.enterprise.gestioneOrdini.dto.BigliettoDto;
import org.unical.enterprise.gestioneOrdini.dto.OrdineDto;
import org.unical.enterprise.shared.clients.MailServiceClient;
import org.unical.enterprise.shared.clients.UtenteServiceClient;
import org.unical.enterprise.shared.dto.MailTransferDto;
import org.unical.enterprise.shared.dto.UtenteDTO;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrdineService {

    private final OrdineDao ordineDao;



    //TODO fare in modo che non siano "istanze volanti" ma che vengano chiamate da un solo punto
    private final MailServiceClient mailServiceClient;

    private final UtenteServiceClient utenteServiceClient;

    private final EventoServiceClient eventoServiceClient;

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
    public void saveOrdine(OrdineDto ordine, List<BigliettoDto> biglietti) throws Exception {
        Ordine newOrdine = new Ordine();
        newOrdine.setUtenteId(ordine.utenteId());
        newOrdine.setBiglietti_comprati(ordine.bigliettiComprati());
        newOrdine.setImporto(ordine.importo());
        newOrdine.setData_pagamento(new Date());


        Set<Biglietto> newBiglietti = new HashSet<>();
        for(BigliettoDto bigliettoDto : biglietti){

            //Controlla se l'id dell'evento esiste altrimenti exception
            eventoServiceClient.findById(bigliettoDto.idEvento());

            Biglietto newBiglietto = new Biglietto();


            //TODO Builder
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


        //TODO Tutto da pulire non deve stare qua a volare

        //TODO quando una diqueste cose non va a buon fine 1 la mail deve cambiare / o non deve inviarsi

        UtenteDTO toUtente = utenteServiceClient.getById(newOrdine.getUtenteId());

        MailTransferDto mailSended = new MailTransferDto(newOrdine.getId() , newOrdine.getData_pagamento(), newOrdine.getImporto(), toUtente.getEmail(), toUtente.getUsername());
        mailServiceClient.sendMail(mailSended);
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