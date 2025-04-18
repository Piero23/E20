package demacs.unical.esse20.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "location")
@Setter
@Getter
@NoArgsConstructor
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Location(String nome, String descrizione, boolean chiuso, String position) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.chiuso = chiuso;
        this.position = position;
    }

    @Column(nullable = false)
    String nome;

    @Column(nullable = false)
    String descrizione;

    @Column(nullable = false)
    boolean chiuso;

    @Column(nullable = false)
    String position;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", orphanRemoval = true,fetch = FetchType.LAZY)
    Set<Evento> eventi = new HashSet<>();

}
