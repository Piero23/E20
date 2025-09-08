package org.unical.enterprise.shared.dto;

import lombok.Builder;

@Builder
public record TicketMailDTO(
        String qr,

        String nome,

        String cognome,

        String nomeEvento
) {}
