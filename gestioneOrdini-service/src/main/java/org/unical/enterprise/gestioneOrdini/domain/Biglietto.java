package org.unical.enterprise.gestioneOrdini.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "biglietto",uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_biglietto",
                columnNames = { "id_evento", "email"}
        )
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Biglietto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UuidGenerator
    @Column(length = 36)
    private UUID id;

    @Column(nullable = false)
    private Long idEvento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean e_valido;

    @Column(length = 50)
    private String nome;

    @Column
    private String cognome;

    @Column
    private Date data_nascita;

    @JsonIgnore //serve ad evitare ricorsioni infinite nelle api non mostrando l'ordine quando si fa la query
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Ordine ordine;
}
