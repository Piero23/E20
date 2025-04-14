package demacs.unical.esse20.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "location")
@Setter
@Getter
public class Location {
    @Id
    private Long id;

    @Column(nullable = false)
    String nome;

    @Column(nullable = false)
    String descrizione;

    @Column(nullable = false)
    boolean chiuso;

    @Column(nullable = false)
    String position;

}
