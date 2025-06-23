package org.unical.enterprise.shared.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.unical.enterprise.shared.FeignConfig;
import org.unical.enterprise.shared.dto.UtenteDTO;

import java.util.UUID;

@FeignClient(name = "utente-service", configuration = FeignConfig.class)
public interface UtenteServiceClient {

    @GetMapping("api/utente/getById")
    UtenteDTO getById(@RequestParam UUID id);
}
