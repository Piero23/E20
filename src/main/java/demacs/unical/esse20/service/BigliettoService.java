package demacs.unical.esse20.service;

import demacs.unical.esse20.dao.BigliettoDao;
import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.domain.Ordine;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BigliettoService {

    private final BigliettoDao bigliettoDao;

    @Transactional
    public List<Biglietto> findAllByOrdine(Ordine ordine){
        return bigliettoDao.findAllByOrdine(ordine);
    }

    @Transactional
    public String getQrCode(String id){
        String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + URLEncoder.encode(id, StandardCharsets.UTF_8);
        byte[] imageBytes = new RestTemplate().getForObject(qrUrl, byte[].class);

        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
