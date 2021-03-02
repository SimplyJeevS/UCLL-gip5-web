package be.ucll.java.gip5.config;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PersoonUserDetailsService implements UserDetailsService {

    private final PersoonRepository persoonRepository;

    public PersoonUserDetailsService(PersoonRepository persoonRepository) {
        this.persoonRepository = persoonRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            return new PersoonPrincipal(persoonRepository.findPersoonByEmailIgnoreCase(s)
                    .orElseThrow(()-> new InvalidCredentialsException()));
        } catch (InvalidCredentialsException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
