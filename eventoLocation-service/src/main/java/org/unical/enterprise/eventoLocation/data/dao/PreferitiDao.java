package org.unical.enterprise.eventoLocation.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unical.enterprise.eventoLocation.data.entities.Preferiti;


public interface PreferitiDao extends JpaRepository<Preferiti, Long> {
}
