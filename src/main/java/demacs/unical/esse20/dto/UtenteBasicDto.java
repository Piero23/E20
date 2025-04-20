package demacs.unical.esse20.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UtenteBasicDto {

    private String id;

    private String username;

    private String email;

    private String password;

    private String dataNascita;

    private String ruolo;
}
