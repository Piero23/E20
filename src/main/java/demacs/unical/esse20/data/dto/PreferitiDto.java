package demacs.unical.esse20.data.dto;


import demacs.unical.esse20.data.entities.Preferiti;
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

    public PreferitiDto(Preferiti preferiti) {
        this.id = preferiti.getId();
        this.utente_id = preferiti.getUtente_id();
        this.evento_id = preferiti.getEvento().getId();
    }
}
