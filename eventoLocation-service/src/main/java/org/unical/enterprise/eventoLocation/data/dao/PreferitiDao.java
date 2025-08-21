package org.unical.enterprise.eventoLocation.data.dao;

import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.unical.enterprise.eventoLocation.data.entities.Preferiti;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PreferitiDao extends JpaRepository<Preferiti, Long> {

    List<Preferiti> findAllByUtenteId(String utenteUUID);

    Optional<Preferiti> findByUtenteIdAndEventoId(String utenteUUID, long eventoId);


}
