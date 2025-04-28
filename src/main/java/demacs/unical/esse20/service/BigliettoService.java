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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BigliettoService {

    private final BigliettoDao bigliettoDao;

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
