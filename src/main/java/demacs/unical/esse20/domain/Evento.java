package demacs.unical.esse20.domain;

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

    public Evento(String descrizione, Long location_id, String nome,Utente organizzatore, Long posti, boolean b_riutilizzabile, boolean b_nominativo) {
        this.descrizione = descrizione;
        this.location_id = location_id;
        this.organizzatore = organizzatore;
        this.nome = nome;
        this.posti = posti;
        this.b_riutilizzabile = b_riutilizzabile;
        this.b_nominativo = b_nominativo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private Long location_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Utente organizzatore;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Long posti;

    //Immagine

    @Column(nullable = false)
    private boolean b_riutilizzabile;

    @Column(nullable = false)
    private boolean b_nominativo;
}
