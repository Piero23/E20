package demacs.unical.esse20.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteLoginDto {

    private String username;

    private String password;
}
