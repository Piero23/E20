package org.unical.enterprise.utente.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.unical.enterprise.shared.dto.UtenteAuthDTO;
import org.unical.enterprise.shared.dto.UtenteDTO;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "utente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utente {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private boolean organizzatore;

    // TODO: Rimuovi Password, lasciala solo in AuthenticationServer.(...).UserAuth
    @Column(length = 500, nullable = false)
    @ToString.Exclude // Don't include password in toString
    private String password;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    // to-DTO Conversions
    public static UtenteDTO toSharedDTO(Utente utente) {
        return UtenteDTO.builder()
                .id(utente.getId())
                .username(utente.getUsername())
                .email(utente.getEmail())
                .dataNascita(utente.getDataNascita())
                .build();
    }

}
