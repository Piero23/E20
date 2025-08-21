package org.unical.enterprise.shared.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EventoBasicDto {

    //TODO Valida con jakartaValidation
    private Long id;
    private String nome;
    private String descrizione;
    //TODO fai a UUID
    private String organizzatore;
    private Long posti;
    private boolean b_riutilizzabile;
    private boolean b_nominativo;
    private Long locationId;
    private Date data;
    @Positive
    private double prezzo;

    /*
    public EventoBasicDto(Evento event) {
        this.id = event.getId();
        this.nome = event.getNome();
        this.descrizione = event.getDescrizione();
        this.organizzatore = event.getOrganizzatore();
        this.posti = event.getPosti();
        this.data = event.getData();
        this.prezzo = event.getPrezzo();

        try {
            this.locationId = event.getLocation().getId();
        }catch(Exception e){
            locationId = null;
        }
    }

     */
}
