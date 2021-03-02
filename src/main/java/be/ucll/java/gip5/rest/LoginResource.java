package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.model.Persoon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/rest/v1")
public class LoginResource {
    private Logger logger = LoggerFactory.getLogger(LoginResource.class);
    private PersoonRepository persoonRepository;

    @Autowired
    public LoginResource(PersoonRepository persoonRepository){
        this.persoonRepository = persoonRepository;
    }

    @PostMapping( value = "/login")
    public ResponseEntity postCheckLogin(@RequestParam(value = "email") String email, @RequestParam("wachtwoord") String wachtwoord) throws InvalidCredentialsException {
        Optional<Persoon> persoon = persoonRepository.findPersoonByEmailAndWachtwoord(email, wachtwoord);
        if(!persoon.isPresent()){
            throw new InvalidCredentialsException();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new PersoonDTO(
                        persoon.get().getVoornaam(),
                        persoon.get().getNaam(),
                        persoon.get().getGeboortedatum().toString(),
                        persoon.get().getGeslacht(),
                        persoon.get().getAdres(),
                        persoon.get().getTelefoon(),
                        persoon.get().getGsm(),
                        persoon.get().getEmail()
                )
        );
    }
}
