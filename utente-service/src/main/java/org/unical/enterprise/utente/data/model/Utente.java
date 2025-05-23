package org.unical.enterprise.utente.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
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

    @Column(length = 500, nullable = false)
    @ToString.Exclude // Don't include password in toString
    private String password;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;
}
