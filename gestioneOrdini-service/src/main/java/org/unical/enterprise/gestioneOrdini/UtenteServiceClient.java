package org.unical.enterprise.gestioneOrdini;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.unical.enterprise.shared.dto.UtenteDTO;

import java.util.UUID;

//TODO Metterlo nello shared
@FeignClient("utente-service")
public interface UtenteServiceClient {

    @GetMapping("api/utente/getById")
    UtenteDTO getById(@RequestParam UUID id);
}
