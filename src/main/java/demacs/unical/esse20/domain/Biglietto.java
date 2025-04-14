package demacs.unical.esse20.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "biglietto")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Biglietto {

    public Biglietto(Long id_evento, String email, boolean e_valido, String nome, String cognome, Date data_nascita, Long ordine_id) {
        this.id_evento = id_evento;
        this.email = email;
        this.e_valido = e_valido;
        this.nome = nome;
        this.cognome = cognome;
        this.data_nascita = data_nascita;
        this.ordine_id = ordine_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_evento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean e_valido;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private Date data_nascita;

    @Column(nullable = false)
    private Long ordine_id;

    //Immagine

    // TODO
    // @OneToMany(fetch = FetchType.LAZY)
    // private Set<Evento> eventi = new HashSet<>();

}
