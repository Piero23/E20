package org.unical.enterprise.eventoLocation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.unical.enterprise.shared.clients.FeignConfig;
import org.unical.enterprise.shared.dto.BigliettoDto;

import java.util.List;

@FeignClient(name = "gestioneOrdini-service", configuration = FeignConfig.class)
public interface BigliettoServiceClient {

    @GetMapping("api/biglietto/evento")
    List<BigliettoDto> getBigliettoEvento(@RequestParam Long id);
}
