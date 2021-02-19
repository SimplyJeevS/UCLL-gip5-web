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
@RequestMapping("/rest/v1")
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

    @GetMapping(value="/deelname/{id}")
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

    @GetMapping( value = "/deelname")
    public ResponseEntity getDeelnameList() throws NotFoundException {
        List<Deelname> deelnameList = deelnameRepository.findAll();
        if(deelnameList.isEmpty()) throw new NotFoundException("Deelnames");
        return ResponseEntity.status(HttpStatus.OK).body(deelnameList);
    }

    @GetMapping( value = "/deelname/wedstrijd/{wedstrijdId}")
    public ResponseEntity getDeelnameWedstrijd(@PathVariable("wedstrijdId") Long wedstrijdId) throws NotFoundException, ParameterInvalidException {
        checkandFindWedstrijdId(wedstrijdId);
        Optional<List<Deelname>> deelnameList = deelnameRepository.findAllByWedstrijdId(wedstrijdId);
        if(!deelnameList.isPresent() || deelnameList.get().isEmpty()){
            throw new NotFoundException("Geen deelnames gevonden voor de wedstrijd met id "+wedstrijdId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(deelnameList.get());
    }

    @GetMapping( value = "/deelname/persoon/{persoonId}")
    public ResponseEntity getDeelnamePersoon(@PathVariable("persoonId") Long persoonId) throws NotFoundException, ParameterInvalidException {
        checkandFindPersoonId(persoonId);
        Optional<List<Deelname>> deelnameList = deelnameRepository.findAllByPersoonId(persoonId);
        if(!deelnameList.isPresent() || deelnameList.get().isEmpty()){
            throw new NotFoundException("Geen deelnames gevonden voor de persoon met id "+persoonId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(deelnameList.get());
    }

    @PutMapping("/deelname/{id}/commentaar")
    public ResponseEntity putDeelnameCommentaar(@PathVariable("id") Long id,@RequestParam(value = "commentaar",defaultValue = "") String commentaar) throws ParameterInvalidException, NotFoundException {
        if(commentaar.trim().length() <= 0){
            throw new ParameterInvalidException("Commentaar mag niet leeg zijn");
        }
        Deelname deelname = checkandFindDeelnameId(id);
        deelname.setCommentaar(commentaar);
        deelnameRepository.save(deelname);
        return ResponseEntity.status(HttpStatus.OK).body(deelname);
    }

    @PostMapping(value="/deelname")
    @Operation(
            summary = "Maak bericht",
            description = "Creer een nieuwe deelname"
    )
    public ResponseEntity postDeelname(@RequestBody DeelnameDTO deelname) throws ParameterInvalidException, NotFoundException {
        logger.debug("POST request voor deelname gekregen");
        if(deelname.getPersoonId().equals(null) || deelname.getWedstrijdId().equals(null) || deelname.getCommentaar().equals(null)){
            throw new ParameterInvalidException("Geef een compleet object mee van deelname");
        }
        if(deelname.getCommentaar().trim().length() <= 0){
            throw new ParameterInvalidException("Commentaar mag niet leeg zijn");
        }
        checkandFindWedstrijdId(deelname.getPersoonId());
        checkandFindWedstrijdId(deelname.getWedstrijdId());
        Deelname newDeelname = deelnameRepository.save(new Deelname.DeelnameBuilder()
        .persoonId(deelname.getPersoonId())
        .wedstrijdId(deelname.getWedstrijdId())
        .commentaar(deelname.getCommentaar())
        .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(newDeelname);
    }

    @PutMapping( value = "/deelname/{id}")
    @Operation(
            summary = "Pas deelname aan",
            description = "verander de rol, persoon en/of ploeg van de deelname"
    )
    public ResponseEntity putDeelname(@PathVariable("id") Long id,@RequestBody DeelnameDTO deelname) throws ParameterInvalidException, NotFoundException {
        if(deelname.getCommentaar().trim().length() <= 0){
            throw new ParameterInvalidException("Deelname is niet ingvuld");
        }
        Deelname foundDeelname = checkandFindDeelnameId(id);
        checkandFindPersoonId(deelname.getPersoonId());
        checkandFindWedstrijdId(deelname.getWedstrijdId());
        foundDeelname.setPersoonId(deelname.getPersoonId());
        foundDeelname.setPersoonId(deelname.getWedstrijdId());
        foundDeelname.setCommentaar(deelname.getCommentaar());
        deelnameRepository.save(foundDeelname);
        return ResponseEntity.status(HttpStatus.OK).body(deelname);
    }
    @DeleteMapping( value = "/deelname/{id}")
    @Operation(
            summary = "Verwijder een deelname",
            description = "Geef het id van de deelname mee om het te verwijderen"
    )
    public ResponseEntity deleteDeelname(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("DELETE request voor deelname gekregen");
        if(id == null || !(id instanceof Long) ||    id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Deelname> deelname = deelnameRepository.findDeelnameById(id);
        if(!deelname.isPresent()){
            throw new NotFoundException(id.toString());
        }
        deelnameRepository.delete(deelname.get());
        return ResponseEntity.status(HttpStatus.OK).body(deelname.get());
    }

    private Wedstrijd checkandFindWedstrijdId(Long id) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(id);
        if(!wedstrijd.isPresent()){
            throw new NotFoundException("Wedstrijd niet gevonden met id "+id);
        }
        return wedstrijd.get();
    }

    private Persoon checkandFindPersoonId(Long id) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Persoon> persoon = persoonRepository.findPersoonById(id);
        if(!persoon.isPresent()){
            throw new NotFoundException("Persoon niet gevonden met id "+id);
        }
        return persoon.get();
    }

    private Deelname checkandFindDeelnameId(Long id) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Deelname> deelname = deelnameRepository.findDeelnameById(id);
        if(!deelname.isPresent()){
            throw new NotFoundException("Deelname niet gevonden met id "+id);
        }
        return deelname.get();
    }
}
