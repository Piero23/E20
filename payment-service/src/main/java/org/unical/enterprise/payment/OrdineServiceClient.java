package org.unical.enterprise.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.unical.enterprise.shared.dto.OrdineRequest;
import org.unical.enterprise.shared.dto.TicketCheck;

import javax.validation.Valid;

@FeignClient(name = "gestioneOrdini-service")
public interface OrdineServiceClient {

    @PostMapping(value="api/ordine")
    ResponseEntity<String> save(@RequestBody OrdineRequest ordineRequest);

    @PostMapping(value="api/biglietto/duplicate")
    boolean findByData(@RequestBody TicketCheck ticketCheck);
}
