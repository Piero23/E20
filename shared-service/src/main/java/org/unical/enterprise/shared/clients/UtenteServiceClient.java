package org.unical.enterprise.shared.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.unical.enterprise.shared.FeignConfig;
import org.unical.enterprise.shared.dto.UtenteDTO;

import java.util.UUID;

@FeignClient(name = "utente-service", configuration = FeignConfig.class)
public interface UtenteServiceClient {

    @GetMapping("/api/utente/id/{utenteId}")
    UtenteDTO getById(@PathVariable UUID utenteId);

}
