package org.unical.enterprise.gestioneOrdini;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.unical.enterprise.shared.dto.EventoBasicDto;

@FeignClient(name = "eventoLocation-service")
public interface EventoServiceClient {

    @GetMapping(value="api/evento/{id}")
    EventoBasicDto findById(@PathVariable("id") Long id);

}
