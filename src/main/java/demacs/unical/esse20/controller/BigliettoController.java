package demacs.unical.esse20.controller;

import demacs.unical.esse20.domain.Biglietto;
import demacs.unical.esse20.service.BigliettoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

//decidere se rimuovere completamente la classe

@RestController
@RequestMapping("/api/biglietto")
@AllArgsConstructor
public class BigliettoController {

    private final BigliettoService bigliettoService;

    private static final Logger logger = LoggerFactory.getLogger(BigliettoController.class);

    @GetMapping
    private ResponseEntity<List<Biglietto>> getAllBiglietti() {
        List<Biglietto> response=new ArrayList<>();
        response.addAll(bigliettoService.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/{id}")
    private ResponseEntity<Map<String, String>> getQrCode(@PathVariable UUID id) throws URISyntaxException {
        //potenzialmente sostituibile nel backend

        //ritorna l'id del biglietto,
        //implementare la validazione allo scan
        logger.info("Richiesta generazione QrCode Ordine");
        Map<String, String> response = new HashMap<>();
        response.put("imageBase64", bigliettoService.getQrCode(id));
        return ResponseEntity.ok(response);
    }
}
