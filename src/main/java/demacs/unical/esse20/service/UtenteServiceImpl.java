package demacs.unical.esse20.service;

import demacs.unical.esse20.dao.UtenteDao;
import demacs.unical.esse20.domain.AuthProvider;
import demacs.unical.esse20.domain.Ruolo;
import demacs.unical.esse20.domain.Utente;

import demacs.unical.esse20.dto.UtenteBasicDto;
import demacs.unical.esse20.dto.UtenteRegistrationDto;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {

    private final UtenteDao utenteDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Utente save(Utente user) { return utenteDao.save(user); }

    @Override
    public void delete(Utente user) { utenteDao.delete(user); }

    @Override
    public List<Utente> getAllUsers() { return utenteDao.findAll(); }

    @Override
    public Optional<Utente> getUserByUsername(String username) { return utenteDao.findByUsername(username); }

    @Override
    public Optional<Utente> getUserByEmail(String email) { return utenteDao.findByEmail(email); }

    @Override
    public Utente registerNewUser(UtenteRegistrationDto userRegistrationDto) {
        Utente userToSave = Utente.builder()
                .username(userRegistrationDto.getUsername())
                .email(userRegistrationDto.getEmail())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .dataNascita(LocalDate.parse(userRegistrationDto.getDataNascita()))
                .ruolo(Ruolo.USER)
                .authProvider(AuthProvider.LOCAL)
                .build();

        return utenteDao.save(userToSave);
    }

    @Override
    public Utente registerNewUser(UtenteRegistrationDto userRegistrationDto, AuthProvider authProvider) {
        if (authProvider.equals(AuthProvider.LOCAL)) return registerNewUser(userRegistrationDto);

        Utente userToSave = Utente.builder()
                .username(userRegistrationDto.getUsername())
                .email(userRegistrationDto.getEmail())
                .password(authProvider.name() + "_AUTH")
                .dataNascita(LocalDate.parse(userRegistrationDto.getDataNascita()))
                .ruolo(Ruolo.USER)
                .authProvider(authProvider)
                .build();

        return utenteDao.save(userToSave);
    }

}
