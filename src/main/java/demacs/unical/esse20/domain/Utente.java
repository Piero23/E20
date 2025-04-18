package demacs.unical.esse20.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Table(name = "utente")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Utente {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean organizzatore;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Date data_nascita;
}
