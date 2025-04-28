package demacs.unical.esse20.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteBasicDto {

    private String id;

    private String username;

    private String email;

    private String password;

    private String dataNascita;

    private String ruolo;

    private String authProvider;
}
