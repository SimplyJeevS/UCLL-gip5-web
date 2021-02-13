package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.BerichtRepository;
import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Bericht;
import be.ucll.java.gip5.dto.BerichtDTO;
import be.ucll.java.gip5.model.Wedstrijd;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BerichtResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private BerichtRepository berichtRepository;
    private WedstrijdRepository wedstrijdRepository;

    @Autowired
    public BerichtResource(BerichtRepository berichtRepository, WedstrijdRepository wedstrijdRepository){
        this.berichtRepository = berichtRepository;
        this.wedstrijdRepository = wedstrijdRepository;
    }

    @GetMapping(value = "/v1/bericht/{id}")
    @Operation(
            summary = "Verkrijg bericht",
            description = "Geef een bericht ID en verkrijg het bericht"
    )
    public ResponseEntity getBericht(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor bericht gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Bericht> bericht =  berichtRepository.findBerichtById(id);
        if(!bericht.isPresent()) {
            throw new NotFoundException(id.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(bericht.get());
    }
    @PostMapping(value = "/v1/bericht")
    @Operation(
            summary = "maak bericht",
            description = "Creeer een nieuw bericht"
    )
    public ResponseEntity postBericht(@RequestBody BerichtDTO bericht) throws ParameterInvalidException, NotFoundException {
        logger.debug("POST request voor bericht gekregen");
        if(bericht.getBoodschap().isEmpty() || bericht.getWedstrijdId() == null){
            throw new ParameterInvalidException(bericht.toString());
        }
        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(bericht.getWedstrijdId());
        if(!wedstrijd.isPresent()){
            throw new NotFoundException("Wedstrijd with id "+bericht.getWedstrijdId());
        }
        Bericht newBericht = berichtRepository.save(new Bericht.BerichtBuilder()
        .boodschap(bericht.getBoodschap())
        .wedstrijdId(bericht.getWedstrijdId())
        .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(newBericht);

    }

    @PutMapping(value="/v1/bericht/{id}")
    @Operation(
            summary = "Pas bericht aan",
            description = "Verander de inhoud van een bericht"
    )
    public ResponseEntity putBericht(@PathVariable("id") Long id,@RequestBody BerichtDTO berichtDto) throws ParameterInvalidException, NotFoundException {
        logger.debug("PUT request voor bericht gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        if(berichtDto.getBoodschap().isEmpty() || berichtDto.getWedstrijdId() == null){
            throw new ParameterInvalidException(berichtDto.toString());
        }
        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(berichtDto.getWedstrijdId());
        if(!wedstrijd.isPresent()){
            throw new NotFoundException("Wedstrijd with id "+berichtDto.getWedstrijdId());
        }
        Optional<Bericht> bericht = berichtRepository.findBerichtById(id);

        if(!bericht.isPresent()){
            throw new NotFoundException(id.toString());
        }
        bericht.get().setBoodschap(berichtDto.getBoodschap());
        bericht.get().setWedstrijdId(berichtDto.getWedstrijdId());
        berichtRepository.save(bericht.get());
        return ResponseEntity.status(HttpStatus.OK).body(new BerichtDTO(bericht.get().getWedstrijdId(),bericht.get().getBoodschap()));
    }

    @DeleteMapping(value="/v1/bericht/{id}")
    @Operation(
            summary = "Verwijder een bericht",
            description= "Geef id een mee en verwijder het bericht"
    )
    public ResponseEntity deleteBericht(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("DELETE request voor bericht gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
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
