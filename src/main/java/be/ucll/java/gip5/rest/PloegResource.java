package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dto.PloegDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Ploeg;
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
public class PloegResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private PloegRepository ploegRepository;

    @Autowired
    public PloegResource(PloegRepository ploegRepository){
        this.ploegRepository = ploegRepository;
    }

    @GetMapping(value = "/v1/ploeg/{id}")
    @Operation(
            summary = "Verkrijg ploeg",
            description = "Geef een ploeg ID en verkrijg de ploeg"
    )
    public ResponseEntity getPloeg(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor ploeg gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Ploeg> ploeg =  ploegRepository.findPloegById(id);
        if(ploeg.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(ploeg.get());
        }else{
            throw new NotFoundException(id.toString());
        }
    }

    @GetMapping( value = "/v1/ploeg")
    public ResponseEntity getPloegen() throws NotFoundException {
        List<Ploeg> ploegen = ploegRepository.findAll();
        if(ploegen.isEmpty()){
            throw new NotFoundException("Geen ploegen gevonden");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ploegen);
    }

    @PostMapping(value="/v1/ploeg")
    public ResponseEntity postPloeg(@RequestBody PloegDTO ploeg) throws ParameterInvalidException {
        checkPloegDTO(ploeg);
        Ploeg newPloeg = ploegRepository.save(
                new Ploeg.PloegBuilder()
                .naam(ploeg.getNaam())
                .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newPloeg);
    }

    @PutMapping( value = "/v1/ploeg/{id}")
    public ResponseEntity putPloeg(@PathVariable("id") Long id,@RequestBody PloegDTO ploeg) throws ParameterInvalidException, NotFoundException {
        if(id == null && !(id instanceof Long) && id <=0 ){
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

    @DeleteMapping( value = "/v1/ploeg/{id}")
    public ResponseEntity deletePloeg(@PathVariable("id") Long id) throws NotFoundException, ParameterInvalidException {
        if(id == null && !(id instanceof Long) && id <=0 ){
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
}
