package demacs.unical.esse20.controller;

import lombok.AllArgsConstructor;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/biglietto")
@AllArgsConstructor
public class BigliettoController {

    @GetMapping(value="/{id}")
    private ResponseEntity<Map<String, String>> getQrCode(@PathVariable String id) throws URISyntaxException {
        //ritorna l'id del biglietto,
        //implementare la validazione allo scan

        String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + URLEncoder.encode(id, StandardCharsets.UTF_8);
        byte[] imageBytes = new RestTemplate().getForObject(qrUrl, byte[].class);

        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        Map<String, String> response = new HashMap<>();
        response.put("imageBase64", base64);
        return ResponseEntity.ok(response);
    }
}
