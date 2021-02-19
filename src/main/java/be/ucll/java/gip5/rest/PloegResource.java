package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.dto.PloegDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Wedstrijd;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/v1")
public class PloegResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private PloegRepository ploegRepository;
    private WedstrijdRepository wedstrijdRepository;

    @Autowired
    public PloegResource(PloegRepository ploegRepository, WedstrijdRepository wedstrijdRepository){
        this.ploegRepository = ploegRepository;
        this.wedstrijdRepository = wedstrijdRepository;
    }

    @GetMapping(value = "/ploeg/{id}")
    @Operation(
            summary = "Verkrijg ploeg",
            description = "Geef een ploeg ID en verkrijg de ploeg"
    )
    public ResponseEntity getPloeg(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor ploeg gekregen");
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Ploeg> ploeg =  ploegRepository.findPloegById(id);
        if(ploeg.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(ploeg.get());
        }else{
            throw new NotFoundException(id.toString());
        }
    }

    @GetMapping( value = "/ploeg")
    public ResponseEntity getPloegen() throws NotFoundException {
        List<Ploeg> ploegen = ploegRepository.findAll();
        if(ploegen.isEmpty()){
            throw new NotFoundException("Geen ploegen gevonden");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ploegen);
    }

    @PostMapping(value="/ploeg")
    public ResponseEntity postPloeg(@RequestBody PloegDTO ploeg) throws ParameterInvalidException {
        checkPloegDTO(ploeg);
        Ploeg newPloeg = ploegRepository.save(
                new Ploeg.PloegBuilder()
                .naam(ploeg.getNaam())
                .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newPloeg);
    }

    @PutMapping( value = "/ploeg/{id}")
    public ResponseEntity putPloeg(@PathVariable("id") Long id,@RequestBody PloegDTO ploeg) throws ParameterInvalidException, NotFoundException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Ploeg> foundPloeg = ploegRepository.findPloegById(id);
        if(!foundPloeg.isPresent()){
            throw new NotFoundException("Ploeg met id "+id);
        }
        checkPloegDTO(ploeg);
        foundPloeg.get().setNaam(ploeg.getNaam());
        ploegRepository.save(foundPloeg.get());
        return ResponseEntity.status(HttpStatus.OK).body(foundPloeg);
    }

    @DeleteMapping( value = "/ploeg/{id}")
    public ResponseEntity deletePloeg(@PathVariable("id") Long id) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Ploeg> foundPloeg = ploegRepository.findPloegById(id);
        if(!foundPloeg.isPresent()){
            throw new NotFoundException("Ploeg met id "+id);
        }
        ploegRepository.delete(foundPloeg.get());
        return ResponseEntity.status(HttpStatus.OK).body(foundPloeg);
    }

    private void checkPloegDTO(PloegDTO ploeg) throws ParameterInvalidException {
        if(ploeg.getNaam().isEmpty() || ploeg.getNaam().trim().length() ==0){
            throw new ParameterInvalidException("Naam met value "+ploeg.getNaam());
        }
    }

    @GetMapping( value = "/ploeg/thuisploeg")
    public ResponseEntity getThuisploegen() throws NotFoundException {
        List<Wedstrijd> wedstrijden = wedstrijdRepository.findAll();
        if(wedstrijden.isEmpty()){
            throw new NotFoundException("Er zijn nog geen wedstrijden gespeeld");
        }
        List<Ploeg> ploegen = Collections.<Ploeg>emptyList();
        wedstrijden.forEach(wedstrijd -> {
            Optional<Ploeg> ploeg = ploegRepository.findPloegById(wedstrijd.getThuisPloeg());
            if(ploeg.isPresent()){
                ploegen.add(ploeg.get());
            }
        });
        if(ploegen.isEmpty()){
            throw new NotFoundException("Thuisploegen in wedstrijden ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ploegen);
    }

    @GetMapping( value = "/ploeg/tegenstander")
    public ResponseEntity getTegenstanders() throws NotFoundException {
        List<Wedstrijd> wedstrijden = wedstrijdRepository.findAll();
        if(wedstrijden.isEmpty()){
            throw new NotFoundException("Er zijn nog geen wedstrijden gespeeld");
        }
        List<Ploeg> ploegen = Collections.<Ploeg>emptyList();
        wedstrijden.forEach(wedstrijd -> {
            Optional<Ploeg> ploeg = ploegRepository.findPloegById(wedstrijd.getTegenstander());
            if(ploeg.isPresent()){
                ploegen.add(ploeg.get());
            }
        });
        if(ploegen.isEmpty()){
            throw new NotFoundException("Tegenstander in wedstrijden ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ploegen);
    }

}
