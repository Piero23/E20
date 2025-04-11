package demacs.unical.esse20;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "utente")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Utente{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;







}
