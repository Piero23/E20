package demacs.unical.esse20.service;

import demacs.unical.esse20.dao.UtenteDao;
import demacs.unical.esse20.domain.Utente;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {

    private final UtenteDao utenteDao;


    @Override
    public Utente save(Utente user) {
        return utenteDao.save(user);
    }

    @Override
    public void delete(Utente user) {
        utenteDao.delete(user);
    }

    @Override
    public List<Utente> getAllUsers() {
        return utenteDao.findAll();
    }

    @Override
    public Optional<Utente> getUserByUsername(String username) {
        return utenteDao.findByUsername(username);
    }

    @Override
    public Optional<Utente> getUserByEmail(String email) {
        return utenteDao.findByEmail(email);
    }
}
