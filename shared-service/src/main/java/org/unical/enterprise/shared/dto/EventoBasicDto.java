package org.unical.enterprise.shared.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EventoBasicDto {

    @Positive
    private Long id;

    @NotBlank(message = "Il campo nome non può essere vuoto.")
    private String nome;

    private String descrizione;

    @NotNull(message="Organizzatore mancante")
    
    private UUID organizzatore;

    @PositiveOrZero(message = "Il numero di posti deve essere 0 o superiore")
    private Long posti;

    private boolean b_riutilizzabile;

    private boolean b_nominativo;

    private boolean age_restricted;

    @Positive(message = "Location mancante")
    private Long locationId;

    @NotNull(message = "La data dell'evento è obbligatoria.")
    private Date data;

    @PositiveOrZero
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
