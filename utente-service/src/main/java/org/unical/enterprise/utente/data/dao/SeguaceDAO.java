package org.unical.enterprise.utente.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unical.enterprise.utente.data.model.Seguace;

import java.util.List;
import java.util.Optional;
// import java.util.UUID;


public interface SeguaceDAO extends JpaRepository<Seguace, Long>  {

//    List<Seguace> findByUtenteSeguace_Id(UUID utenteSeguaceId);
//    List<Seguace> findByUtenteSeguito_Id(UUID utenteSeguitoId);

    List<Seguace> findAllByUtenteSeguace_Username(String username);
    List<Seguace> findAllByUtenteSeguito_Username(String username);

    boolean existsByUtenteSeguace_UsernameAndUtenteSeguito_Username(String usernameUtenteSeguace, String usernameUtenteSeguito);

    Optional<Seguace> findByUtenteSeguace_UsernameAndUtenteSeguito_Username(String usernameUtenteSeguace, String usernameUtenteSeguito);


}
