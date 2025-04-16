package demacs.unical.esse20.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Entity
@Table(name = "ordine")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Ordine {

    public Ordine(Utente utente, Long biglietti_comprati, float importo, Date data_pagamento) {
        this.utente = utente;
        this.biglietti_comprati = biglietti_comprati;
        this.importo = importo;
        this.data_pagamento = data_pagamento;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @Column(nullable = false)
    private Long biglietti_comprati;

    @Column(nullable = false)
    private float importo;

    @Column(nullable = false)
    private Date data_pagamento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordine_id", orphanRemoval = true,fetch = FetchType.EAGER)
    Set<Biglietto> biglietti = new HashSet<>();

    public boolean addBiglietto(Biglietto biglietto) {
        try {
            biglietti.add(biglietto);
            biglietto.setOrdine_id(this);

            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}