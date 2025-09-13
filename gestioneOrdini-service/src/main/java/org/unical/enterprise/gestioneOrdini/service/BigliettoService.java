package org.unical.enterprise.gestioneOrdini.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.unical.enterprise.gestioneOrdini.dao.BigliettoDao;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;
import org.unical.enterprise.shared.clients.EventoServiceClient;
import org.unical.enterprise.shared.clients.UtenteServiceClient;
import org.unical.enterprise.shared.dto.BigliettoDto;

import java.net.CacheRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BigliettoService {

    private final BigliettoDao bigliettoDao;

    private final EventoServiceClient eventoServiceClient;

    private final UtenteServiceClient utenteServiceClient;

    @Transactional
    public List<Biglietto> findAll(){return bigliettoDao.findAll();}

    @Transactional
    public List<Biglietto> findAllByOrdine(Ordine ordine){
        return bigliettoDao.findAllByOrdine(ordine);
    }

    @Transactional
    public String getQrCode(UUID id){
        String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=1000x1000&data=" + URLEncoder.encode(id.toString(), StandardCharsets.UTF_8);
        byte[] imageBytes = new RestTemplate().getForObject(qrUrl, byte[].class);

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    @Transactional
    public boolean findTicketByData(String email, Long evento){
        if (bigliettoDao.existsByEmailAndIdEvento(email, evento))
            return true;
        return false;
    }

    @Transactional
    public List<BigliettoDto> findAllByEvento(Long id) {
        List<Biglietto> biglietti = bigliettoDao.findAllByIdEvento(id);

        List<BigliettoDto> bigliettiDto = new ArrayList<>();
        for (Biglietto biglietto : biglietti) {
            BigliettoDto b = new BigliettoDto(id, biglietto.getEmail(), biglietto.isE_valido(), biglietto.getNome(), biglietto.getCognome(), biglietto.getData_nascita());
            bigliettiDto.add(b);
        }
        return bigliettiDto;
    }

    @Transactional
    public boolean validate(UUID id){
        Biglietto ticket = bigliettoDao.findById(id).get();
        if (ticket.isE_valido() && !eventoServiceClient.findById(ticket.getIdEvento()).isB_riutilizzabile()){
            ticket.setE_valido(false);
            bigliettoDao.save(ticket);
            return true;
        }
        else return eventoServiceClient.findById(ticket.getIdEvento()).isB_riutilizzabile();
    }

    @Transactional
    public boolean checkExists(UUID id){
        return bigliettoDao.existsById(id);
    }

    @Transactional
    public UUID getUserIDByUsername(String username) {
        return utenteServiceClient.getUtenteByUsername(username).getBody().getId();
    }

    @Transactional
    public UUID getOrganizzatoreEventoByTicket(UUID id) {
        return eventoServiceClient.findById(bigliettoDao.findById(id).get().getIdEvento()).getOrganizzatore();
    }

    @Transactional
    public BigliettoDto findById(UUID id) {
        Biglietto b=bigliettoDao.findById(id).get();
        return BigliettoDto.builder()
                .nome(b.getNome())
                .cognome(b.getCognome())
                .email(b.getEmail())
                .idEvento(b.getIdEvento())
                .data_nascita(b.getData_nascita())
                .e_valido(b.isE_valido())
                .build();
    }
}
