package org.unical.enterprise.shared.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.shared.FeignConfig;
import org.unical.enterprise.shared.dto.UtenteDTO;

import java.util.UUID;

@FeignClient(name = "utente-service", configuration = FeignConfig.class)
public interface UtenteServiceClient {

    @GetMapping("/api/utente/id/{utenteId}")
    UtenteDTO getById(@PathVariable UUID utenteId);

    @PostMapping("api/utente/register")
    UtenteDTO register(@RequestBody UtenteDTO utenteDTO);

    @DeleteMapping("api/utente/{username}")
    void delete(@PathVariable String username);

}
