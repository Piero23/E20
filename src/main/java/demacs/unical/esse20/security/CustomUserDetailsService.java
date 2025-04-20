package demacs.unical.esse20.security;

import demacs.unical.esse20.dao.UtenteDao;
import demacs.unical.esse20.domain.Ruolo;
import demacs.unical.esse20.domain.Utente;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* Questa classe permette di definine durante l'autenticazioe l'utente che richiede l'accesso.
* Oltre a fornire le informazioni necessarie, definisce anche il ruolo che l'utente ha nell'applicazione.
*/
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteDao utenteDao;

    public CustomUserDetailsService(final UtenteDao utenteDao) {
        this.utenteDao = utenteDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Utente utenteCercato = utenteDao.findbyUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username non trovato"));

        return new User(utenteCercato.getUsername(),
                        utenteCercato.getPassword(),
                        mapRoleToAuthorities(utenteCercato.getRuolo()));
    }

    private List<GrantedAuthority> mapRoleToAuthorities(final Ruolo ruolo) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + ruolo.name()));
    }
}
