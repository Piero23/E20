package demacs.unical.esse20.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "evento")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Evento {

    public Evento(String descrizione, Location location , String nome, String organizzatore, Long posti, boolean b_riutilizzabile, boolean b_nominativo) {
        this.descrizione = descrizione;
        this.location = location;
        this.organizzatore = organizzatore;
        this.nome = nome;
        this.posti = posti;
        this.b_riutilizzabile = b_riutilizzabile;
        this.b_nominativo = b_nominativo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 500)
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Location location;

    @Column(nullable = false, length = 36)
    private String organizzatore;

    @Column(nullable = false)
    private String nome;

    @Column
    private Long posti;

    @Column(nullable = false)
    private boolean b_riutilizzabile;

    @Column(nullable = false)
    private boolean b_nominativo;
}
