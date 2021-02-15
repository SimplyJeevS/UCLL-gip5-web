package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.*;
import be.ucll.java.gip5.dto.DeelnameDTO;
import be.ucll.java.gip5.dto.ToewijzingDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.*;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DeelnameResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private DeelnameRepository deelnameRepository;
    private PersoonRepository persoonRepository;
    private WedstrijdRepository wedstrijdRepository;

    @Autowired
    public DeelnameResource(DeelnameRepository deelnameRepository, PersoonRepository persoonRepository, WedstrijdRepository wedstrijdRepository){
        this.deelnameRepository = deelnameRepository;
        this.persoonRepository = persoonRepository;
        this.wedstrijdRepository = wedstrijdRepository;
    }

    @GetMapping(value="/v1/deelname/{id}")
    @Operation(
            summary = "Verkrijg deelname",
            description = "Geef een deelname ID en verkrijg de deelname"
    )
    public ResponseEntity getDeelname(@PathVariable("deelname") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor deelname gekregen");
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Deelname> deelname = deelnameRepository.findDeelnameById(id);
        if(!deelname.isPresent()){
            throw new NotFoundException(id.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deelname.get());
    }

    @GetMapping( value = "/v1/deelname")
    public ResponseEntity getDeelnameList() throws NotFoundException {
        List<Deelname> deelnameList = deelnameRepository.findAll();
        if(deelnameList.isEmpty()) throw new NotFoundException("Deelnames");
        return ResponseEntity.status(HttpStatus.OK).body(deelnameList);
    }

    @GetMapping( value = "/v1/deelname/wedstrijd/{wedstrijdId}")
    public ResponseEntity getDeelnameWedstrijd(){
        //TODO
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("");
    }

    @PostMapping(value="/v1/deelname")
    @Operation(
            summary = "Maak bericht",
            description = "Creer een nieuwe deelname"
    )
    public ResponseEntity postDeelname(@RequestBody DeelnameDTO deelname) throws ParameterInvalidException, NotFoundException {
        logger.debug("POST request voor deelname gekregen");
        if(deelname.getPersoonId().equals(null) || deelname.getWedstrijdId().equals(null) || deelname.getCommentaar().equals(null)){
            throw new ParameterInvalidException("Geef een compleet object mee van deelname");
        }
        if(deelname.getPersoonId() <= 0 || deelname.getWedstrijdId() <= 0){
            throw new ParameterInvalidException("Id mag niet minder als 0 zijn");
        }
        if(deelname.getCommentaar().trim().length() <= 0){
            throw new ParameterInvalidException("Deelname is niet ingvuld");
        }
        Optional<Persoon> persoon = persoonRepository.findPersoonById(deelname.getPersoonId());
        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(deelname.getWedstrijdId());
        if(!persoon.isPresent()){
            throw new NotFoundException("Persoon met id "+deelname.getPersoonId());
        }
        if(!wedstrijd.isPresent()){
            throw new NotFoundException("Wedstrijd met id "+deelname.getWedstrijdId());
        }
        Deelname newDeelname = deelnameRepository.save(new Deelname.DeelnameBuilder()
        .persoonId(deelname.getPersoonId())
        .wedstrijdId(deelname.getWedstrijdId())
        .commentaar(deelname.getCommentaar())
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
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        if(deelname.equals(null)){
            throw new ParameterInvalidException("Geef een deelname object mee");
        }
        if(deelname.getPersoonId() <= 0 || deelname.getWedstrijdId() <= 0){
            throw new ParameterInvalidException("Id mag niet minder als 0 zijn");
        }
        if(deelname.getCommentaar().trim().length() <= 0){
            throw new ParameterInvalidException("Deelname is niet ingvuld");
        }
        Optional<Deelname> foundDeelname = deelnameRepository.findDeelnameById(id);
        Optional<Persoon> persoon = persoonRepository.findPersoonById(deelname.getPersoonId());
        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(deelname.getWedstrijdId());
        if(!foundDeelname.isPresent()){
            throw new NotFoundException("Deelname met id "+id);
        }
        if(!persoon.isPresent()){
            throw new NotFoundException("Persoon met id "+deelname.getPersoonId());
        }
        if(!wedstrijd.isPresent()){
            throw new NotFoundException("Ploeg met id "+deelname.getWedstrijdId());
        }
        foundDeelname.get().setPersoonId(deelname.getPersoonId());
        foundDeelname.get().setPersoonId(deelname.getWedstrijdId());
        foundDeelname.get().setCommentaar(deelname.getCommentaar());
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
        if(id == null || !(id instanceof Long) || id <=0 ){
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
