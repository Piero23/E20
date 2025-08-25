package org.unical.enterprise.utente.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeguaceRequestDTO {

    @NotBlank(message = "Lo username è obbligatorio.")
    private String username;
}
