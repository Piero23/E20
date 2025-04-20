package demacs.unical.esse20.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UtenteLoginDto {

    private String username;

    private String password;
}
