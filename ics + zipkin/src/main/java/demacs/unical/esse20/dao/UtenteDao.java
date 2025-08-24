package demacs.unical.esse20;

import demacs.unical.esse20.domain.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteDao extends JpaRepository<Utente, Long> {

}
