package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dto.LoginDTO;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
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

    //inloggen, zou een sessie starten met de huidige rol
    @PostMapping( value = "/login")
    public ResponseEntity postCheckLogin(@RequestBody LoginDTO loginDTO) throws InvalidCredentialsException {
        logger.debug(loginDTO.toString());
        Optional<Persoon> persoon = persoonRepository.findPersoonByEmailAndWachtwoord(loginDTO.getEmail(), loginDTO.getWachtwoord());
        if(!persoon.isPresent()){
            System.out.println("not found");
            throw new InvalidCredentialsException();
        }
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpSession session= attr.getRequest().getSession(true); // true == allow create
        session.setAttribute("api",persoon.get().getApi());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new PersoonDTO(
                        persoon.get().getId(),
                        persoon.get().getVoornaam(),
                        persoon.get().getNaam(),
                        persoon.get().getGeboortedatum().toString(),
                        persoon.get().getGeslacht(),
                        persoon.get().getAdres(),
                        persoon.get().getTelefoon(),
                        persoon.get().getGsm(),
                        persoon.get().getEmail(),
                        persoon.get().getDefault_rol(),
                        persoon.get().getApi()
                )
        );
    }
}
