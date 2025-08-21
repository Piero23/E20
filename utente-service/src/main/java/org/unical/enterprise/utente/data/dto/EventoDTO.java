package org.unical.enterprise.utente.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoDTO {

    private Long id;
    private String nome;
    private String descrizione;
    private String organizzatore;
    private Long posti;
    private boolean b_riutilizzabile;
    private boolean b_nominativo;
    private double prezzo;

    // public static EventoDTO toDTO()
}
