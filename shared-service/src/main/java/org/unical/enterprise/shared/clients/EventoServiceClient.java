package org.unical.enterprise.shared.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.unical.enterprise.shared.dto.EventoBasicDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "eventoLocation-service", configuration = FeignConfig.class)
public interface EventoServiceClient {

    @PostMapping("/api/preferiti/utente/{utenteId}/evento/{eventoId}")
    void aggiungiAiPreferiti(@PathVariable UUID utenteId, @PathVariable Long eventoId);

    @DeleteMapping("/api/preferiti/utente/{utenteId}/evento/{eventoId}")
    void rimuoviDaiPreferiti(@PathVariable UUID utenteId, @PathVariable Long eventoId);

    @GetMapping("/api/preferiti/utente/{utenteId}")
    List<EventoBasicDto> getAllPreferiti(@PathVariable UUID utenteId);

    @DeleteMapping("/api/preferiti/utente/{utenteId}")
    void deleteListaPreferiti(@PathVariable UUID utenteId);

    @GetMapping(value="api/evento/{id}")
    EventoBasicDto findById(@PathVariable("id") Long id);

}
