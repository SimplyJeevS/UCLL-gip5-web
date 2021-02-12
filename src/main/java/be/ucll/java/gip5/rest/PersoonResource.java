package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Persoon;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/persoon")
public class PersoonResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private PersoonRepository persoonRepository;

    @Autowired
    public PersoonResource(PersoonRepository persoonRepository){
        this.persoonRepository = persoonRepository;
    }

    @GetMapping(value = "{id}")
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

}
