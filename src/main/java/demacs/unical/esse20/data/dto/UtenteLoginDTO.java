package demacs.unical.esse20.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtenteLoginDTO {
    @Email(message = "L'indirizzo email non è valido.")
    @NotBlank(message = "L'indirizzo email è obbligatorio.")
    private String email;

    @NotBlank(message = "La password è obbligatoria.")
    private String password;
}
