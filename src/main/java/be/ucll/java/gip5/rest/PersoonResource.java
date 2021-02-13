package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dao.RolRepository;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Rol;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PersoonResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private PersoonRepository persoonRepository;
    private PloegRepository ploegRepository;
    private RolRepository rolRepository;

    @Autowired
    public PersoonResource(PersoonRepository persoonRepository,PloegRepository ploegRepository, RolRepository rolRepository){
        this.persoonRepository = persoonRepository;
        this.ploegRepository = ploegRepository;
        this.rolRepository = rolRepository;
    }

    @GetMapping(value = "/v1/persoon/{id}")
    @Operation(
            summary = "Verkrijg persoon",
            description = "Geef een persoon ID en verkrijg de persoon"
    )
    public ResponseEntity getPersoon(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor persoon gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Persoon> persoon =  persoonRepository.findPersoonById(id);
        if(persoon.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(persoon.get());
        }else{
            throw new NotFoundException(id.toString());
        }
    }

    @PostMapping(value = "/v1/persoon")
    @Operation(
            summary = "Maak een persoon",
            description = "Creeer een nieuwe persoon"
    )
    public ResponseEntity postPersoon(@PathVariable PersoonDTO persoon) throws ParameterInvalidException, NotFoundException {
        logger.debug("POST request voor persoon gekregen");
        if(persoon.getGeslacht() == null || !(persoon.getGeslacht() instanceof Boolean)){
            throw new ParameterInvalidException("Geslacht met waarde "+persoon.getGeslacht());
        }
        if(persoon.getAdres() == null || persoon.getAdres().trim().length() == 0){
            throw new ParameterInvalidException("Adress met waarde "+persoon.getAdres());
        }
        if(persoon.getEmail() == null || persoon.getEmail().trim().length() == 0){
            throw new ParameterInvalidException("E-mail met waarde "+persoon.getEmail());
        }
        if(persoon.getGeboortedatum() == null || persoon.getGeboortedatum().toString().trim().length() == 0){
            throw new ParameterInvalidException("Geboortedatum met waarde "+persoon.getGeboortedatum());
        }
        if(persoon.getGeboortedatum().after(new Date())){
            throw new ParameterInvalidException("Geboortedatum ligt niet in het verleden, "+persoon.getGeboortedatum());
        }
        if(persoon.getGsm() == null || persoon.getGsm().trim().length() == 0){
            throw new ParameterInvalidException("Gsm met waarde "+persoon.getGsm());
        }
        if(persoon.getNaam() == null || persoon.getNaam().trim().length() == 0){
            throw new ParameterInvalidException("Naam met waarde "+persoon.getNaam());
        }
        if(persoon.getTelefoon() == null || persoon.getTelefoon().trim().length() == 0){
            throw new ParameterInvalidException("Telefoon met waarde "+persoon.getTelefoon());
        }
        if(persoon.getWachtwoord() == null || persoon.getWachtwoord().trim().length() < 8){
            throw new ParameterInvalidException("Wachtwoord moet minstens 8 characters bevatten, u gaf "+persoon.getWachtwoord());
        }
        Optional<Ploeg> ploeg = Optional.empty();
        if(persoon.getPloegId() == null || !(persoon.getPloegId() instanceof Long)){
            //throw new ParameterInvalidException("Ploeg id met waarde "+persoon.getPloegId());
        }else{
            ploeg = ploegRepository.findPloegById(persoon.getPloegId());
        }
        if(persoon.getRolId() == null || !(persoon.getRolId() instanceof Long)){
            throw new ParameterInvalidException("Rol id met waarde "+persoon.getRolId());
        }

        Optional<Rol> rol = rolRepository.findRolById(persoon.getRolId());
        if(!ploeg.isPresent()){
            throw new NotFoundException("Ploeg met id "+persoon.getPloegId());
        }
        if(!rol.isPresent()){
            throw new NotFoundException("Rol met id "+persoon.getRolId());
        }
        Persoon newPersoon = persoonRepository.save(
                new Persoon.PersoonBuilder()
                .adres(persoon.getAdres())
                .email(persoon.getEmail())
                .geboortedatum(persoon.getGeboortedatum())
                .geslacht(persoon.getGeslacht())
                .gsm(persoon.getGsm())
                .voornaam(persoon.getVoornaam())
                .naam(persoon.getNaam())
                .wachtwoord(persoon.getWachtwoord())
                .telefoon(persoon.getTelefoon())
                .rolId(persoon.getRolId())
                .ploegId(persoon.getPloegId())
                .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newPersoon);
    }

    @PutMapping (value = "/v1/persoon/{id}")
    @Operation(
            summary = "Pas persoon aan",
            description = "Verander data van een persoon"
    )
    public ResponseEntity putPersoon(){

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("");
    }

    @DeleteMapping(value = "/v1/persoon/{id}")
    @Operation(
            summary = "Verwijder een persoon",
            description = "Geef het id van een persoon mee om het te verwijderen"
    )
    public ResponseEntity deletePersoon(){

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("");
    }
}
