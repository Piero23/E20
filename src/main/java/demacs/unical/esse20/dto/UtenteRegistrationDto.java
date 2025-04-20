package demacs.unical.esse20.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UtenteRegistrationDto {

    private String username;

    private String email;

    private String password;

    private String dataNascita;

}
