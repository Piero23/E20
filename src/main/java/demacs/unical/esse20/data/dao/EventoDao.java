package demacs.unical.esse20.data.dao;

import demacs.unical.esse20.data.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventoDao extends JpaRepository<Evento, Long> {

}
