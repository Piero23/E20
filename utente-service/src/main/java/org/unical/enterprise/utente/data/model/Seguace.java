package org.unical.enterprise.utente.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "segue")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seguace_id")
    private Utente utenteSeguace;

    @ManyToOne
    @JoinColumn(name = "seguito_id")
    private Utente utenteSeguito;

}
