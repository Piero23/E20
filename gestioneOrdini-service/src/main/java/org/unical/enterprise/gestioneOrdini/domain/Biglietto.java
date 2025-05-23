package org.unical.enterprise.gestioneOrdini.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@ToString
public class Biglietto {

    public Biglietto(Long id_evento, String email, boolean e_valido, String nome, String cognome, Date data_nascita) {
        this.id_evento = id_evento;
        this.email = email;
        this.e_valido = e_valido;
        this.nome = nome;
        this.cognome = cognome;
        this.data_nascita = data_nascita;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UuidGenerator
    @Column(length = 36)
    private UUID id;

    @Column(nullable = false)
    private Long id_evento;

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
