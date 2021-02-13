package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.DeelnameRepository;
import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dao.RolRepository;
import be.ucll.java.gip5.dto.DeelnameDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Deelname;
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

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DeelnameResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private DeelnameRepository deelnameRepository;
    private PersoonRepository persoonRepository;
    private PloegRepository ploegRepository;
    private RolRepository rolRepository;

    @Autowired
    public DeelnameResource(DeelnameRepository deelnameRepository,PersoonRepository persoonRepository,PloegRepository ploegRepository,RolRepository rolRepository){
        this.deelnameRepository = deelnameRepository;
        this.persoonRepository = persoonRepository;
        this.ploegRepository = ploegRepository;
        this.rolRepository = rolRepository;
    }

    @GetMapping(value="/v1/deelname/{id}")
    @Operation(
            summary = "Verkrijg deelname",
            description = "Geef een deelname ID en verkrijg de deelname"
    )
    public ResponseEntity getDeelname(@PathVariable("deelname") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor deelname gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Deelname> deelname = deelnameRepository.findDeelnameById(id);
        if(!deelname.isPresent()){
            throw new NotFoundException(id.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deelname.get());
    }

    @PostMapping(value="/v1/deelname")
    @Operation(
            summary = "Maak bericht",
            description = "Creer een nieuwe deelname"
    )
    public ResponseEntity postDeelname(@RequestBody DeelnameDTO deelname) throws ParameterInvalidException, NotFoundException {
        logger.debug("POST request voor deelname gekregen");
        if(deelname.getPersoonId() ==null || deelname.getPersoonId() <= 0){
            throw new ParameterInvalidException(deelname.getPersoonId().toString());
        }
        if(deelname.getPloegId() == null || deelname.getPersoonId() <= 0){
            throw new ParameterInvalidException(deelname.getPersoonId().toString());
        }
        if(deelname.getRolId() == null || deelname.getRolId() <= 0 ){
            throw new ParameterInvalidException(deelname.getRolId().toString());
        }
        Optional<Persoon> persoon = persoonRepository.findPersoonById(deelname.getPersoonId());
        Optional<Ploeg> ploeg = ploegRepository.findPloegById(deelname.getPloegId());
        Optional<Rol> rol = rolRepository.findRolById(deelname.getRolId());
        if(!persoon.isPresent()){
            throw new NotFoundException("Persoon met id "+deelname.getPersoonId());
        }
        if(!ploeg.isPresent()){
            throw new NotFoundException("Ploeg met id "+deelname.getPloegId());
        }
        if(!rol.isPresent()){
            throw new NotFoundException("Rol met id "+deelname.getRolId());
        }
        Deelname newDeelname = deelnameRepository.save(new Deelname.DeelnameBuilder()
        .persoonId(deelname.getPersoonId())
        .ploegId(deelname.getPloegId())
        .rolId(deelname.getRolId())
        .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(newDeelname);
    }

    @PutMapping( value = "/v1/deelname/{id}")
    @Operation(
            summary = "Pas deelname aan",
            description = "verander de rol, persoon en/of ploeg van de deelname"
    )
    public ResponseEntity putDeelname(@PathVariable("id") Long id,@RequestBody DeelnameDTO deelname) throws ParameterInvalidException, NotFoundException {
        logger.debug("PUT request voor deelname gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        if(deelname.equals(null)){
            throw new ParameterInvalidException("Geef een deelname object mee");
        }
        if(deelname.getPersoonId().equals(null) || deelname.getPloegId().equals(null) || deelname.getRolId().equals(null)){
            throw new ParameterInvalidException("Geef een compleet object mee van deelname");
        }
        if(deelname.getPersoonId() <= 0 || deelname.getPloegId() <= 0 || deelname.getRolId() <= 0){
            throw new ParameterInvalidException("Id mag niet minder als 0 zijn");
        }
        Optional<Deelname> foundDeelname = deelnameRepository.findDeelnameById(id);
        Optional<Persoon> persoon = persoonRepository.findPersoonById(deelname.getPersoonId());
        Optional<Ploeg> ploeg = ploegRepository.findPloegById(deelname.getPloegId());
        Optional<Rol> rol = rolRepository.findRolById(deelname.getRolId());
        if(!foundDeelname.isPresent()){
            throw new NotFoundException("Deelname met id "+id);
        }
        if(!persoon.isPresent()){
            throw new NotFoundException("Persoon met id "+deelname.getPersoonId());
        }
        if(!ploeg.isPresent()){
            throw new NotFoundException("Ploeg met id "+deelname.getPloegId());
        }
        if(!rol.isPresent()){
            throw new NotFoundException("Rol met id "+deelname.getRolId());
        }
        foundDeelname.get().setPersoonId(deelname.getPersoonId());
        foundDeelname.get().setPloegId(deelname.getPloegId());
        foundDeelname.get().setRolId(deelname.getRolId());
        deelnameRepository.save(foundDeelname.get());
        return ResponseEntity.status(HttpStatus.OK).body(deelname);
    }
    @DeleteMapping( value = "/v1/deelname/{id}")
    @Operation(
            summary = "Verwijder een deelname",
            description = "Geef het id van de deelname mee om het te verwijderen"
    )
    public ResponseEntity deleteDeelname(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("DELETE request voor deelname gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Deelname> deelname = deelnameRepository.findDeelnameById(id);
        if(!deelname.isPresent()){
            throw new NotFoundException(id.toString());
        }
        deelnameRepository.delete(deelname.get());
        return ResponseEntity.status(HttpStatus.OK).body(deelname.get());
    }
}
