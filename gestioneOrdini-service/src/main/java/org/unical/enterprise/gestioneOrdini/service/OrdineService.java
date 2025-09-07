package org.unical.enterprise.gestioneOrdini.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unical.enterprise.shared.clients.EventoServiceClient;
import org.unical.enterprise.gestioneOrdini.dao.OrdineDao;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;
import org.unical.enterprise.shared.dto.*;
import org.unical.enterprise.shared.clients.MailServiceClient;
import org.unical.enterprise.shared.clients.UtenteServiceClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrdineService {

    private final OrdineDao ordineDao;
    private final BigliettoService bigliettoService;

    private final MailServiceClient mailServiceClient;

    private final UtenteServiceClient utenteServiceClient;

    private final EventoServiceClient eventoServiceClient;

    @Transactional
    public List<Ordine> findAll() {
        return ordineDao.findAll();
    }

    @Transactional
    public void saveOrdine(OrdineRequest ordineRequest) {
        Ordine newOrdine = Ordine.builder()
                .utenteId(ordineRequest.ordine().utenteId())
                .importo(ordineRequest.ordine().importo())
                .data_pagamento(new Date())
                .build();


        Set<Biglietto> newBiglietti = new HashSet<>();
        for(BigliettoDto bigliettoDto : ordineRequest.biglietti()){

            try {
                eventoServiceClient.findById(bigliettoDto.idEvento());
            }catch(Exception e){
                throw new RuntimeException("L'evento che stai provando a prenotare non esiste.");
            }

            Biglietto newBiglietto = Biglietto.builder()
                    .ordine(newOrdine)
                    .email(bigliettoDto.email())
                    .nome(bigliettoDto.nome())
                    .cognome(bigliettoDto.cognome())
                    .data_nascita(bigliettoDto.dataNascita())
                    .e_valido(bigliettoDto.eValido())
                    .idEvento(bigliettoDto.idEvento())
                    .build();

            newBiglietti.add(newBiglietto);
        }

        newOrdine.setBiglietti(newBiglietti);
        newOrdine.setBiglietti_comprati(newBiglietti.size());

        ordineDao.save(newOrdine);

        MailTransferDto mailSended = new MailTransferDto(newOrdine.getId() , newOrdine.getData_pagamento(), newOrdine.getImporto(), ordineRequest.mailTo(), ordineRequest.nome());
        mailServiceClient.sendMail(mailSended);

        for (Biglietto biglietto : newBiglietti) {
            mailServiceClient.sendQrCodeMail(biglietto.getEmail(), bigliettoService.getQrCode(biglietto.getId()));
        }
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

    @Transactional
    public UUID getUserIDByUsername(String username) {
        return utenteServiceClient.getUtenteByUsername(username).getBody().getId();
    }


}