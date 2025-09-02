package org.unical.enterprise.utente.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unical.enterprise.shared.dto.UtenteDTO;

import java.time.LocalDate;
import java.util.UUID;

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
