package demacs.unical.esse20.dao;

import demacs.unical.esse20.domain.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteDao extends JpaRepository<Utente, Long> {

}
