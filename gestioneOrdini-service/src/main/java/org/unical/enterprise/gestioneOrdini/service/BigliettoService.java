package org.unical.enterprise.gestioneOrdini.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.unical.enterprise.gestioneOrdini.dao.BigliettoDao;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;

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
}
