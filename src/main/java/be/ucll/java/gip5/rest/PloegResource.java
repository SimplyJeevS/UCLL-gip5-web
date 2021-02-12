package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Ploeg;
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
@RequestMapping("/ploeg")
public class PloegResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private PloegRepository ploegRepository;

    @Autowired
    public PloegResource(PloegRepository ploegRepository){
        this.ploegRepository = ploegRepository;
    }

    @GetMapping(value = "{id}")
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
}
