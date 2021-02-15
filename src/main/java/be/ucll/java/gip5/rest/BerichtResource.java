package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.BerichtRepository;
import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.dto.WedstrijdDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Bericht;
import be.ucll.java.gip5.dto.BerichtDTO;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Wedstrijd;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/bericht")
public class BerichtResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private BerichtRepository berichtRepository;
    private WedstrijdRepository wedstrijdRepository;
    private PersoonRepository persoonRepository;
    @Autowired
    public BerichtResource(BerichtRepository berichtRepository, WedstrijdRepository wedstrijdRepository,PersoonRepository persoonRepository){
        this.berichtRepository = berichtRepository;
        this.wedstrijdRepository = wedstrijdRepository;
        this.persoonRepository = persoonRepository;
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Verkrijg bericht",
            description = "Geef een bericht ID en verkrijg het bericht"
    )
    public ResponseEntity getBericht(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor bericht gekregen");
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Bericht> bericht =  berichtRepository.findBerichtById(id);
        if(!bericht.isPresent()) {
            throw new NotFoundException(id.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(bericht.get());
    }

    @GetMapping( value = "/")
    @Operation(
            summary = "Verkrijg alle berichten",
            description = ""
    )
    public ResponseEntity getBerichtList() throws NotFoundException {
        List<Bericht> berichtList = berichtRepository.findAll();
        if(berichtList.isEmpty()){
            throw new NotFoundException("Geen berichten gevonden");
        }
        return ResponseEntity.status(HttpStatus.OK).body(berichtList);
    }

    @GetMapping( value = "/wedstrijd/{wedstrijdId}")
    public ResponseEntity getBerichtListFromWedstrijdId(@PathVariable("wedstrijdId") Long wedstrijdId) throws ParameterInvalidException, NotFoundException {
        if(wedstrijdId == null || !(wedstrijdId instanceof Long) || wedstrijdId <=0 ) {
            throw new ParameterInvalidException(wedstrijdId.toString());
        }
        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(wedstrijdId);
        if(!(wedstrijd.isPresent())){
            throw new NotFoundException("Wedstrijd met waarde "+wedstrijdId);
        }
        Optional<List<Bericht>> berichtList = berichtRepository.findAllByWedstrijdId(wedstrijdId);
        if(!(berichtList.isPresent())){
            throw new NotFoundException("Geen berichten gevonden met wedstrijd id "+wedstrijdId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(berichtList);
    }

    @GetMapping( value = "/afzender/{afzenderId}")
    public ResponseEntity getBerichtListFromAfzenderId(@PathVariable("afzenderId") Long afzenderId) throws ParameterInvalidException, NotFoundException {
        if(afzenderId == null || !(afzenderId instanceof Long) || afzenderId <=0 ) {
            throw new ParameterInvalidException(afzenderId.toString());
        }
        Optional<Persoon> afzender = persoonRepository.findPersoonById(afzenderId);
        if(!(afzender.isPresent())){
            throw new NotFoundException("Afzender met waarde "+afzenderId);
        }
        Optional<List<Bericht>> berichtList = berichtRepository.findAllByAfzenderId(afzenderId);
        if(!(berichtList.isPresent())){
            throw new NotFoundException("Geen berichten gevonden met afzender id "+afzenderId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(berichtList);
    }

    @PostMapping(value = "/")
    @Operation(
            summary = "maak bericht",
            description = "Creeer een nieuw bericht"
    )
    public ResponseEntity postBericht(@RequestBody BerichtDTO bericht) throws ParameterInvalidException, NotFoundException {
        logger.debug("POST request voor bericht gekregen");
        if(bericht.getBoodschap().isEmpty() || bericht.getWedstrijdId() == null){
            throw new ParameterInvalidException(bericht.toString());
        }
        LocalDateTime tijdstip;
        try {
            try {
                tijdstip = LocalDateTime.parse(bericht.getTijdstip(),
                        DateTimeFormatter.ISO_INSTANT);
            } catch (Exception err) {
                tijdstip = LocalDateTime.parse(bericht.getTijdstip(),
                        DateTimeFormatter.RFC_1123_DATE_TIME);
            }
        }catch(Exception err){
            throw new ParameterInvalidException("Tijdstip formaat invalid, gebruik ISO 8601 of RFC 1123/ RFC 822 formaat. tijdstip met waarde "+bericht.getTijdstip());
        }
        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(bericht.getWedstrijdId());
        if(!wedstrijd.isPresent()){
            throw new NotFoundException("Wedstrijd with id "+bericht.getWedstrijdId());
        }
        Optional<Persoon> persoon = persoonRepository.findPersoonById(bericht.getAfzenderId());
        if(!persoon.isPresent()){
            throw new NotFoundException("Persoon with id"+bericht.getAfzenderId().toString());
        }
        Bericht newBericht = berichtRepository.save(new Bericht.BerichtBuilder()
        .boodschap(bericht.getBoodschap())
        .wedstrijdId(bericht.getWedstrijdId())
        .tijdstip(tijdstip)
        .afzenderId(bericht.getAfzenderId())
        .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(newBericht);

    }

    @PutMapping(value="/{id}")
    @Operation(
            summary = "Pas bericht aan",
            description = "Verander de inhoud van een bericht"
    )
    public ResponseEntity putBericht(@PathVariable("id") Long id, @RequestBody BerichtDTO bericht) throws ParameterInvalidException, NotFoundException {
        logger.debug("PUT request voor bericht gekregen");
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }

        if(bericht.getBoodschap().isEmpty() || bericht.getBoodschap().trim().length() <= 0){
            throw new ParameterInvalidException(bericht.getBoodschap());
        }
        if(!(bericht.getAfzenderId() instanceof Long) || bericht.getAfzenderId() <= 0){
            throw new ParameterInvalidException("Afzender is is niet correct, moet een positief nummer zijn. afzenderId met waarde "+bericht.getAfzenderId());
        }
        if(bericht.getWedstrijdId() == null || bericht.getWedstrijdId() <= 0){
            throw new ParameterInvalidException(bericht.getWedstrijdId().toString());
        }

        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(bericht.getWedstrijdId());
        if(!wedstrijd.isPresent()){
            throw new NotFoundException("Wedstrijd with id "+bericht.getWedstrijdId());
        }

        Optional<Bericht> foundBericht = berichtRepository.findBerichtById(id);
        if(!foundBericht.isPresent()){
            throw new NotFoundException(id.toString());
        }
        LocalDateTime tijdstip;
        try {
            try {
                tijdstip = LocalDateTime.parse(bericht.getTijdstip(),
                        DateTimeFormatter.ISO_INSTANT);
            } catch (Exception err) {
                tijdstip = LocalDateTime.parse(bericht.getTijdstip(),
                        DateTimeFormatter.RFC_1123_DATE_TIME);
            }
        }catch(Exception err){
            throw new ParameterInvalidException("Tijdstip formaat invalid, gebruik ISO 8601 of RFC 1123/ RFC 822 formaat. tijdstip met waarde "+bericht.getTijdstip());
        }

        foundBericht.get().setBoodschap(bericht.getBoodschap());
        foundBericht.get().setWedstrijdId(bericht.getWedstrijdId());
        foundBericht.get().setAfzenderId(bericht.getAfzenderId());
        foundBericht.get().setTijdstip(tijdstip);
        berichtRepository.save(foundBericht.get());
        return ResponseEntity.status(HttpStatus.OK).body(new BerichtDTO(
                bericht.getWedstrijdId(),
                bericht.getBoodschap(),
                foundBericht.get().getAfzenderId(),
                bericht.getTijdstip()
        ));
    }

    @PutMapping( value = "/{id}/boodschap")
    public ResponseEntity putBerichtBoodschap(@PathVariable("id") Long id, @RequestBody String boodschap) throws ParameterInvalidException, NotFoundException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Bericht> bericht = berichtRepository.findBerichtById(id);
        if(!(bericht.isPresent())) {
            throw new NotFoundException("Bericht met id " + id);
        }
        if(boodschap.isEmpty() || !(boodschap instanceof String) || boodschap.trim().length() <= 0){
            throw new ParameterInvalidException("Boodschap met waarde "+boodschap);
        }
        bericht.get().setBoodschap(boodschap);
        berichtRepository.save(bericht.get());
        return ResponseEntity.status(HttpStatus.OK).body(bericht.get());
    }

    @DeleteMapping(value="/{id}")
    @Operation(
            summary = "Verwijder een bericht",
            description= "Geef id een mee en verwijder het bericht"
    )
    public ResponseEntity deleteBericht(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("DELETE request voor bericht gekregen");
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Bericht> bericht = berichtRepository.findBerichtById(id);
        if(!bericht.isPresent()) {
            throw new NotFoundException(id.toString());
        }
        berichtRepository.delete(bericht.get());
        return ResponseEntity.status(HttpStatus.OK).body(bericht.get());
    }

}
