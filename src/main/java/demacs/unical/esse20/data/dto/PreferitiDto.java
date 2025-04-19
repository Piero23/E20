package demacs.unical.esse20.data.dto;


import demacs.unical.esse20.data.entities.Preferiti;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferitiDto {
    String utente_id;
    long evento_id;
    boolean status;

    public PreferitiDto(Preferiti preferiti) {
        this.utente_id = preferiti.getUtente_id();
        this.evento_id = preferiti.getEvento().getId();
        this.status = preferiti.isStatus();
    }
}
