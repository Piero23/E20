package demacs.unical.esse20.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
public class Location extends DomainObject<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Location(String nome, String descrizione, boolean chiuso, String position) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.chiuso = chiuso;
        this.position = position;
    }

    @Size(min = 5, max = 50)
    @Column(nullable = false)
    String nome;

    @Size(min = 20, max = 400)
    @Column(nullable = false)
    String descrizione;

    @Column(nullable = false)
    boolean chiuso;

    @Column(nullable = false)
    String position;

    @JsonIgnore
    @OneToMany( mappedBy = "location",fetch = FetchType.LAZY)
    Set<Evento> eventi = new HashSet<>();

}


