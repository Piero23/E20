package demacs.unical.esse20.data.model;

import demacs.unical.esse20.domain.Evento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
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
    @UuidGenerator
    @GeneratedValue
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

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "organizzatore")
    private Set<Evento> eventiOrganizzati = new HashSet<>();
}
