package org.unical.enterprise.eventoLocation.data.dto;


import org.unical.enterprise.eventoLocation.data.entities.Preferiti;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PreferitiDto {
    long id;
    String utente_id;
    long evento_id;

    //TODO ToDTO
    public PreferitiDto(Preferiti preferiti) {
        this.id = preferiti.getId();
        this.utente_id = preferiti.getUtenteId();
        this.evento_id = preferiti.getEvento().getId();
    }

    public static PreferitiDto fromIds(String utenteId, long eventoId) {
        PreferitiDto dto = new PreferitiDto();
        dto.setUtente_id(utenteId);
        dto.setEvento_id(eventoId);
        return dto;
    }
}
