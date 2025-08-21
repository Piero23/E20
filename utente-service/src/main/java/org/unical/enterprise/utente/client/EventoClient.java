package org.unical.enterprise.utente.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.unical.enterprise.utente.data.dto.EventoDTO;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "evento-service")
public interface EventoClient {

    // Post Mapping della API
    void aggiungiAiPreferiti(@PathVariable UUID utenteId, @PathVariable Long eventoId);

    // Delete Mapping della API
    void rimuoviDaiPreferiti(@PathVariable UUID utenteId, @PathVariable Long eventoId);

    // Get Mapping della API
    List<EventoDTO> getAllPreferiti(@PathVariable UUID utenteId);

}
