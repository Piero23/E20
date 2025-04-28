package demacs.unical.esse20.service;

import demacs.unical.esse20.data.dao.UtenteDao;
import demacs.unical.esse20.data.domain.AuthProvider;
import demacs.unical.esse20.data.domain.Ruolo;
import demacs.unical.esse20.data.domain.Utente;

import demacs.unical.esse20.data.dto.UtenteRegistrationDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteDao utenteDao;
    private final PasswordEncoder passwordEncoder;

    
    public Utente save(Utente user) { return utenteDao.save(user); }

    
    public void delete(Utente user) { utenteDao.delete(user); }

    
    public List<Utente> getAllUsers() { return utenteDao.findAll(); }

    
    public Optional<Utente> getUserByUsername(String username) { return utenteDao.findByUsername(username); }

    
    public Optional<Utente> getUserByEmail(String email) { return utenteDao.findByEmail(email); }

    
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
